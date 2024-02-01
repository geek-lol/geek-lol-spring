package com.nat.geeklolspring.shorts.shortsreply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.ShortsReply;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsUpdateRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyListResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.repository.ShortsReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.task.DelegatingSecurityContextAsyncTaskExecutor;
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

    // 댓글 정보들을 돌려주는 서비스
    public ShortsReplyListResponseDTO retrieve(Long shortsId) {
        log.warn("retrieve Execute! - {}", shortsId);
        // 해당 쇼츠Id에 달린 모든 댓글 리스트를 replyList에 저장
        List<ShortsReply> replyList = shortsReplyRepository.findAllByShortsId(shortsId);

        // replyList를 정제해서 allReply에 저장
        List<ShortsReplyResponseDTO> allReply = replyList.stream()
                .map(ShortsReplyResponseDTO::new)
                .collect(Collectors.toList());

        return ShortsReplyListResponseDTO.builder()
                .reply(allReply)
                .build();
    }

    public ShortsReplyListResponseDTO retrievePaging(Long shortsId, Pageable pageInfo) {
        log.warn("retreieve 페이징처리 실행! Id: {}, PageInfo: {}", shortsId, pageInfo);

        Pageable pageable = PageRequest.of(pageInfo.getPageNumber() - 1, pageInfo.getPageSize());

        Page<ShortsReply> replyList = shortsReplyRepository.findAll(pageable);

        List<ShortsReplyResponseDTO> allReply = replyList.stream()
                .map(ShortsReplyResponseDTO::new)
                .collect(Collectors.toList());

        return ShortsReplyListResponseDTO
                .builder()
                .reply(allReply)
                .totalPages(replyList.getTotalPages())
                .totalCount(replyList.getTotalElements())
                .build();
    }

    // 쇼츠 댓글 저장 서비스
    public ShortsReplyListResponseDTO insertShortsReply(
            Long id,
            ShortsPostRequestDTO dto,
            TokenUserInfo userInfo,
            Pageable pageainfo) {
        log.debug("쇼츠 댓글 저장 서비스 실행!");

        // dto에 담겨 있던 내용을 ShortsReply 형식으로 변환해 reply에 저장
        ShortsReply reply = dto.toEntity(id);
        // 현재 유저 정보의 id와 닉네임을 꺼내서 reply에 저장
        reply.setWriterId(userInfo.getUserId());
        reply.setWriterName(userInfo.getUserName());

        // DB에 저장
        shortsReplyRepository.save(reply);

        // 새 댓글이 DB에 저장된 댓글 리스트를 리턴
        return retrievePaging(id, pageainfo);
    }

    // 쇼츠 댓글 삭제 서비스
    public ShortsReplyListResponseDTO deleteShortsReply(Long shortsId,
                                                        Long replyId,
                                                        TokenUserInfo userInfo,
                                                        Pageable pageInfo) {
        // 전달받은 댓글Id의 모든 정보를 가져오기
        ShortsReply reply = shortsReplyRepository.findById(replyId).orElseThrow();

        try {
            boolean flag = EqualsId(reply.getWriterId(), userInfo);
            // 토큰의 id와 댓글의 작성자 id가 같으면 실행
            if(flag)
                // 삭제하지 못하면 Exception 발생
                 shortsReplyRepository.deleteById(replyId);
            else
                throw new NotEqualTokenException("댓글 작성자만 삭제할 수 있습니다!");

            // 댓글이 삭제된 DB에서 가져온 댓글 리스트를 리턴
            return retrievePaging(shortsId, pageInfo);
        } catch (Exception e) {
            log.error("삭제에 실패했습니다. - ID: {}, Error: {}", replyId, e.getMessage());
            throw new RuntimeException("해당 아이디를 가진 댓글이 없습니다!");
        }
    }

    // 쇼츠 댓글 수정 서비스
    public ShortsReplyListResponseDTO updateReply(ShortsUpdateRequestDTO dto,
                                                  TokenUserInfo userInfo,
                                                  Pageable pageInfo) {

        ShortsReply reply = shortsReplyRepository.findById(dto.getReplyId()).orElseThrow();

        // 현재 접속한 유저의 id와 쓴 사람의 id를 비교
        // 일치하면 true
        // 일치하지 않으면 false
        boolean flag = EqualsId(reply.getWriterId(), userInfo);

        if(flag) {
            // dto 안에 들어가있는 id값으로 찾은 댓글의 모든 정보를 target에 저장
            Optional<ShortsReply> target = shortsReplyRepository.findById(dto.getReplyId());

            // ifPresent로 null체크, null이 아니면 중괄호 안의 코드 실행
            target.ifPresent(t -> {
                t.setContext(dto.getContext()); // 수정 댓글 내용을 저장
                t.setModify(t.getModify()+1); // modify 횟수 증가
                shortsReplyRepository.save(t); // 수정된 내용 DB에 저장
            });
        } else
            throw new NotEqualTokenException("댓글 작성자만 수정할 수 있습니다!");

        // 수정된 내용을 불러오기 위해 해당 쇼츠의 댓글 다시 불러오기(refresh)
        // target이 null이어도 실행함
        return retrievePaging(dto.getShortsId(), pageInfo);
    }
}
