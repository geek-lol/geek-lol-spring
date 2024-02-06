package com.nat.geeklolspring.troll.ruling.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.RulingReply;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingReplyPostRequestDTO;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingReplyUpdateRequestDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingReplyListResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingReplyResponseDTO;
import com.nat.geeklolspring.troll.ruling.repository.RulingReplyRepository;
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
public class RulingReplyService {
    private final RulingReplyRepository rulingReplyRepository;

    // 댓글 정보들을 돌려주는 서비스

    public RulingReplyListResponseDTO retrieve(Long rulingId, Pageable pageInfo) {
        log.warn("retreieve 페이징처리 실행! Id: {}, PageInfo: {}", rulingId, pageInfo);

        // 페이징 처리 시 첫번째 페이지는 0으로 시작하니 전달받은 페이지번호 - 1을 페이징 정보로 저장
        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());

        // shortsId로 가져온 해당 쇼츠의 댓글 페이징 처리 정보를 저장
        Page<RulingReply> replyList = rulingReplyRepository.findAllByRulingId(rulingId,pageInfo);

        // 정보를 가공하여 List<DTO>형태로 저장
        List<RulingReplyResponseDTO> allReply = replyList.stream()
                .map(RulingReplyResponseDTO::new)
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

    // 쇼츠 댓글 저장 서비스
    public RulingReplyListResponseDTO insertShortsReply(
            Long id,
            RulingReplyPostRequestDTO dto,
            TokenUserInfo userInfo,
            Pageable pageInfo) {
        log.debug("쇼츠 댓글 저장 서비스 실행!");

        // dto에 담겨 있던 내용을 ShortsReply 형식으로 변환해 reply에 저장
        RulingReply reply = dto.toEntity(id);
        // 현재 유저 정보의 id와 닉네임을 꺼내서 reply에 저장
        reply.setWriterId(userInfo.getUserId());
        reply.setWriterName(userInfo.getUserName());

        // DB에 저장
        rulingReplyRepository.save(reply);

        // 새 댓글이 DB에 저장된 댓글 리스트를 리턴
        return retrieve(id, pageInfo);
    }

    // 쇼츠 댓글 삭제 서비스
    public void deleteShortsReply(Long replyId, TokenUserInfo userInfo) {
        // 전달받은 댓글Id의 모든 정보를 가져오기
        RulingReply reply = rulingReplyRepository.findById(replyId).orElseThrow();

        try {
            boolean flag = EqualsId(reply.getWriterId(), userInfo);
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

    // 쇼츠 댓글 수정 서비스
    public void updateReply(RulingReplyUpdateRequestDTO dto,
                            TokenUserInfo userInfo) {

        RulingReply reply = rulingReplyRepository.findById(dto.getReplyId()).orElseThrow();

        // 현재 접속한 유저의 id와 쓴 사람의 id를 비교
        // 일치하면 true
        // 일치하지 않으면 false
        boolean flag = EqualsId(reply.getWriterId(), userInfo);

        if(flag) {
            // dto 안에 들어가있는 id값으로 찾은 댓글의 모든 정보를 target에 저장
            Optional<RulingReply> target = rulingReplyRepository.findById(dto.getReplyId());

            // ifPresent로 null체크, null이 아니면 중괄호 안의 코드 실행
            target.ifPresent(t -> {
                t.setContext(dto.getContext()); // 수정 댓글 내용을 저장
                t.setModify(t.getModify()+1); // modify 횟수 증가
                rulingReplyRepository.save(t); // 수정된 내용 DB에 저장
            });
        } else
            throw new NotEqualTokenException("댓글 작성자만 수정할 수 있습니다!");
    }
}
