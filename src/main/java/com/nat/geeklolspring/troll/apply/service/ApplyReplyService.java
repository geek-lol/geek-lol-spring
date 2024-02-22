package com.nat.geeklolspring.troll.apply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.ShortsReply;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsUpdateRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyListResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyDeleteRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyReplyPostRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyReplyUpdateRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.ApplyReplyListResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.response.ApplyReplyResponseDTO;
import com.nat.geeklolspring.troll.apply.repository.ApplyReplyRepository;
import com.nat.geeklolspring.troll.apply.repository.RulingApplyRepository;
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
public class ApplyReplyService {
    private final ApplyReplyRepository applyReplyRepository;
    private final RulingApplyRepository rulingApplyRepository;
    private final UserRepository userRepository;

    // 댓글 정보들을 돌려주는 서비스
    public ApplyReplyListResponseDTO retrieve(Long applyId, Pageable pageInfo) {
        log.warn("retreieve 페이징처리 실행! Id: {}, PageInfo: {}", applyId, pageInfo);

        // 페이징 처리 시 첫번째 페이지는 0으로 시작하니 전달받은 페이지번호 - 1을 페이징 정보로 저장
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());
        BoardApply boardApply = rulingApplyRepository.findById(applyId).orElseThrow();
        // shortsId로 가져온 해당 쇼츠의 댓글 페이징 처리 정보를 저장
        Page<ApplyReply> replyList = applyReplyRepository.findAllByApplyId(boardApply,pageable);

        // 정보를 가공하여 List<DTO>형태로 저장
        List<ApplyReplyResponseDTO> allReply = replyList.stream()
                .map(ApplyReplyResponseDTO::new)
                .collect(Collectors.toList());

        if(pageInfo.getPageNumber() > 1 && allReply.isEmpty()) {
            // 페이징 처리된 페이지의 최대값보다 높게 요청시 에러 발생시키기
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return ApplyReplyListResponseDTO
                    .builder()
                    .reply(allReply)
                    .totalPages(replyList.getTotalPages())
                    .totalCount(replyList.getTotalElements())
                    .build();
        }
    }

    @Transactional
    // 댓글 저장 서비스
    public ApplyReplyListResponseDTO insertReply(
            Long applyId,
            ApplyReplyPostRequestDTO dto,
            TokenUserInfo userInfo,
            Pageable pageInfo) {
        log.debug("쇼츠 댓글 저장 서비스 실행!");
        BoardApply boardApply = rulingApplyRepository.findById(applyId).orElseThrow();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        // dto에 담겨 있던 내용을 변환해 reply에 저장
        ApplyReply reply = dto.toEntity(boardApply,user);

        // DB에 저장
        ApplyReply save = applyReplyRepository.save(reply);

        // 새 댓글이 DB에 저장된 댓글 리스트를 리턴
        return retrieve(applyId, pageInfo);
    }

    @Transactional
    // 댓글 삭제 서비스
    public void deleteShortsReply(ApplyDeleteRequestDTO dto, TokenUserInfo userInfo) {
            ApplyReply reply = applyReplyRepository.findById(dto.getId()).orElseThrow();

            try {
                boolean flag = EqualsId(reply.getUserId().getId(), userInfo) || userInfo.getRole().toString().equals("ADMIN");
                // 토큰의 id와 댓글의 작성자 id가 같으면 실행
                if (flag)
                    // 삭제하지 못하면 Exception 발생
                    applyReplyRepository.deleteById(dto.getId());
                else
                    throw new NotEqualTokenException("댓글 작성자만 삭제할 수 있습니다!");

            } catch (Exception e) {
                log.error("삭제에 실패했습니다. - ID: {}, Error: {}", dto.getId(), e.getMessage());
                throw new RuntimeException("해당 아이디를 가진 댓글이 없습니다!");
            }
    }

    //내가 쓴 댓글 조회
    public ApplyReplyListResponseDTO findMyReply(TokenUserInfo userInfo, Pageable pageInfo) {
        String userId = userInfo.getUserId();
        // 페이징 처리 시 첫번째 페이지는 0으로 시작하니 전달받은 페이지번호 - 1을 페이징 정보로 저장
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());

        log.warn("findMyReply 페이징처리 실행! Id: {}, PageInfo: {}", userId, pageInfo);
        User me = userRepository.findById(userId).orElseThrow();
        // shortsId로 가져온 해당 쇼츠의 댓글 페이징 처리 정보를 저장
        Page<ApplyReply> replyList = applyReplyRepository.findByUserId(me,pageable);

        List<ApplyReplyResponseDTO> allReply = replyList.stream()
                .map(reply -> {
                    ApplyReplyResponseDTO dto = new ApplyReplyResponseDTO(reply);
                    BoardApply boardApply = rulingApplyRepository.findById(dto.getApplyId()).orElse(null);
                    if (boardApply != null) {
                        dto.setTitle(boardApply.getTitle());
                    }
                    return dto;
                })
                .collect(Collectors.toList());

        log.warn("replyList:{}",allReply);
        if(pageInfo.getPageNumber() > 1 && allReply.isEmpty()) {
            // 페이징 처리된 페이지의 최대값보다 높게 요청시 에러 발생시키기
            throw new BadRequestException("비정상적인 접근입니다!");
        } else {
            return ApplyReplyListResponseDTO
                    .builder()
                    .reply(allReply)
                    .totalPages(replyList.getTotalPages())
                    .totalCount(replyList.getTotalElements())
                    .build();
        }
    }
}
