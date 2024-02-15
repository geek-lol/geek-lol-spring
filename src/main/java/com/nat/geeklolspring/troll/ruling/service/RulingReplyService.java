package com.nat.geeklolspring.troll.ruling.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingReply;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingReplyPostRequestDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingReplyListResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingReplyResponseDTO;
import com.nat.geeklolspring.troll.ruling.repository.BoardRulingRepository;
import com.nat.geeklolspring.troll.ruling.repository.RulingReplyRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RulingReplyService {
    private final RulingReplyRepository rulingReplyRepository;
    private final BoardRulingRepository boardRulingRepository;
    private final UserRepository userRepository;

    // 댓글 정보들을 돌려주는 서비스
    @Transactional
    public RulingReplyListResponseDTO retrieve(Long rulingId, Pageable pageInfo) {
        try {
            log.warn("retrieve 페이징 처리 실행! Id: {}, PageInfo: {}", rulingId, pageInfo);

            // 페이징 처리 시 첫 번째 페이지는 0으로 시작하니 전달받은 페이지 번호 - 1을 페이징 정보로 저장
            Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
            BoardRuling boardRuling = boardRulingRepository.findById(rulingId).orElseThrow();

            // rulingId로 가져온 해당 ruling의 댓글 페이징 처리 정보를 저장
            Page<RulingReply> replyList = rulingReplyRepository.findAllByRulingId(boardRuling, pageable);

            // 정보를 가공하여 List<DTO> 형태로 저장
            List<RulingReplyResponseDTO> allReply = replyList.stream()
                    .map(RulingReplyResponseDTO::new)
                    .collect(Collectors.toList());

            if (pageInfo.getPageNumber() > 1 && allReply.isEmpty()) {
                // 페이징 처리된 페이지의 최대 값보다 높게 요청시 에러 발생시키기
                throw new BadRequestException("비정상적인 접근입니다!");
            } else {
                return RulingReplyListResponseDTO
                        .builder()
                        .reply(allReply)
                        .totalPages(replyList.getTotalPages())
                        .totalCount(replyList.getTotalElements())
                        .build();
            }
        } catch (BadRequestException e) {
            log.error("Bad request exception: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An unexpected error occurred: {}", e.getMessage());
            throw new RuntimeException("Internal Server Error", e);
        }
    }
    @Transactional
    // 댓글 저장 서비스
    public RulingReplyListResponseDTO insertShortsReply(
            Long id,
            RulingReplyPostRequestDTO dto,
            TokenUserInfo userInfo,
            Pageable pageInfo) {
        log.debug("투표 댓글 저장 서비스 실행!");

        BoardRuling boardRuling = boardRulingRepository.findById(id).orElseThrow();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        // dto에 담겨 있던 내용을 ShortsReply 형식으로 변환해 reply에 저장
        RulingReply reply = dto.toEntity(boardRuling,user);

        // DB에 저장
        rulingReplyRepository.save(reply);

        // 새 댓글이 DB에 저장된 댓글 리스트를 리턴
        return retrieve(id, pageInfo);
    }

    @Transactional
    //댓글 삭제 서비스
    public void deleteShortsReply(Long replyId, TokenUserInfo userInfo) {
        // 전달받은 댓글Id의 모든 정보를 가져오기
        RulingReply reply = rulingReplyRepository.findById(replyId).orElseThrow();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        try {
            boolean flag = reply.getRulingWriterId().equals(user);
            // 토큰의 id와 댓글의 작성자 id가 같으면 실행
            if(flag)
                // 삭제하지 못하면 Exception 발생
                rulingReplyRepository.deleteById(replyId);
            else
                throw new NotEqualTokenException("댓글 작성자만 삭제할 수 있습니다!");

        } catch (Exception e) {
            log.error("삭제에 실패했습니다. - ID: {}, Error: {}", replyId, e.getMessage());
            throw new RuntimeException("해당 아이디를 가진 댓글이 없습니다!");
        }
    }
    //내가 쓴 댓글 조회
    public RulingReplyListResponseDTO findMyReply(TokenUserInfo userInfo, Pageable pageInfo) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        // 페이징 처리 시 첫번째 페이지는 0으로 시작하니 전달받은 페이지번호 - 1을 페이징 정보로 저장
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());

        log.warn("findMyReply 페이징처리 실행! Id: {}, PageInfo: {}", user, pageInfo);
        // shortsId로 가져온 해당 쇼츠의 댓글 페이징 처리 정보를 저장
        Page<RulingReply> replyList = rulingReplyRepository.findByRulingWriterId(user,pageable);

        // 정보를 가공하여 List<DTO>형태로 저장
        List<RulingReplyResponseDTO> allReply = replyList.stream()
                .map(reply -> {
                    RulingReplyResponseDTO dto = new RulingReplyResponseDTO(reply);
                    boardRulingRepository.findById(dto.getRulingId()).ifPresent(boardRuling -> dto.setTitle(boardRuling.getTitle()));
                    return dto;
                })
                .collect(Collectors.toList());

        if(pageInfo.getPageNumber() > 1 && allReply.isEmpty()) {
            // 페이징 처리된 페이지의 최대값보다 높게 요청시 에러 발생시키기
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return RulingReplyListResponseDTO
                    .builder()
                    .reply(allReply)
                    .totalPages(replyList.getTotalPages())
                    .totalCount(replyList.getTotalElements())
                    .build();
        }
    }
}
