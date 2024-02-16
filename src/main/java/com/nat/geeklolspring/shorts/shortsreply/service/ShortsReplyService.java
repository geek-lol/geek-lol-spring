package com.nat.geeklolspring.shorts.shortsreply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.entity.ShortsReply;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsUpdateRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyListResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyMyPageResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.repository.ShortsReplyRepository;
import com.nat.geeklolspring.troll.apply.dto.response.ApplyReplyResponseDTO;
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

import static com.nat.geeklolspring.utils.token.TokenUtil.EqualsId;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ShortsReplyService {
    private final ShortsReplyRepository shortsReplyRepository;
    private final ShortsRepository shortsRepository;
    private final UserRepository userRepository;

    // 댓글 정보들을 돌려주는 서비스

    public ShortsReplyListResponseDTO retrieve(Long shortsId, Pageable pageInfo) {
        log.warn("retrieve 페이징처리 실행! Id: {}, PageInfo: {}", shortsId, pageInfo);
        
        // 페이징 처리 시 첫번째 페이지는 0으로 시작하니 전달받은 페이지번호 - 1을 페이징 정보로 저장
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        BoardShorts shorts = shortsRepository.findByShortsId(shortsId);
        // shortsId로 가져온 해당 쇼츠의 댓글 페이징 처리 정보를 저장
        Page<ShortsReply> replyList = shortsReplyRepository.findAllByShortsIdOrderByReplyDateDesc(shorts, pageable);

        // 정보를 가공하여 List<DTO>형태로 저장
        List<ShortsReplyResponseDTO> allReply = replyList.stream()
                .map(ShortsReplyResponseDTO::new)
                .collect(Collectors.toList());

        if(pageInfo.getPageNumber() > 1 && allReply.isEmpty()) {
            // 페이징 처리된 페이지의 최대값보다 높게 요청시 에러 발생시키기
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return ShortsReplyListResponseDTO
                    .builder()
                    .reply(allReply)
                    .totalPages(replyList.getTotalPages())
                    .totalCount(replyList.getTotalElements())
                    .build();
        }
    }

    @Transactional
    // 쇼츠 댓글 저장 서비스
    public ShortsReplyListResponseDTO insertShortsReply(
            Long shortsId,
            ShortsPostRequestDTO dto,
            TokenUserInfo userInfo,
            Pageable pageInfo) {
        log.debug("쇼츠 댓글 저장 서비스 실행!");

        BoardShorts findShorts = shortsRepository.findById(shortsId).orElseThrow();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();

        // dto에 담겨 있던 내용을 ShortsReply 형식으로 변환해 reply에 저장
        ShortsReply reply = dto.toEntity(user,findShorts);

        // DB에 저장
        shortsReplyRepository.save(reply);
        shortsRepository.upReplyCount(shortsId);

        // 새 댓글이 DB에 저장된 댓글 리스트를 리턴
        return retrieve(shortsId, pageInfo);
    }

    @Transactional
    // 쇼츠 댓글 삭제 서비스
    public void deleteShortsReply(Long replyId, TokenUserInfo userInfo) {
        // 전달받은 댓글Id의 모든 정보를 가져오기
        ShortsReply reply = shortsReplyRepository.findById(replyId).orElseThrow();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        try {
            boolean flag = reply.getWriterId().equals(user);
            // 토큰의 id와 댓글의 작성자 id가 같으면 실행
            if(flag) {
                // 삭제하지 못하면 Exception 발생
                shortsReplyRepository.deleteById(replyId);
                shortsRepository.downReplyCount(reply.getShortsId().getShortsId());
            }
            else
                throw new NotEqualTokenException("댓글 작성자만 삭제할 수 있습니다!");

        } catch (Exception e) {
            log.error("삭제에 실패했습니다. - ID: {}, Error: {}", replyId, e.getMessage());
            throw new RuntimeException("해당 아이디를 가진 댓글이 없습니다!");
        }
    }

    //내가 쓴 댓글 조회
    public ShortsReplyListResponseDTO findMyReply(TokenUserInfo userInfo, Pageable pageInfo){
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        Page<ShortsReply> shortsList = shortsReplyRepository.findAllByWriterId(user,pageable);

        List<ShortsReplyMyPageResponseDTO> myShorts = shortsList.stream()
                .map(reply -> {
                    ShortsReplyMyPageResponseDTO dto = new ShortsReplyMyPageResponseDTO(reply);
                    shortsRepository.findById(dto.getShortId()).ifPresent(shorts -> dto.setTitle(shorts.getTitle()));
                    return dto;
                })
                .collect(Collectors.toList());

        // shortsList를 정제해서 저장
        return ShortsReplyListResponseDTO
                .builder()
                .myreplys(myShorts)
                .totalPages(shortsList.getTotalPages())
                .build();
    }
}
