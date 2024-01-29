package com.nat.geeklolspring.shorts.shortsreply.service;

import com.nat.geeklolspring.entity.ShortsReply;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsUpdateRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyListResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.repository.ShortsReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ShortsReplyService {
    private final ShortsReplyRepository shortsReplyRepository;

    public ShortsReplyListResponseDTO retrieve(Long shortsId) {
        List<ShortsReply> replyList = shortsReplyRepository.findAllByShortsId(shortsId);

        List<ShortsReplyResponseDTO> allReply = replyList.stream()
                .map(ShortsReplyResponseDTO::new)
                .collect(Collectors.toList());

        return ShortsReplyListResponseDTO.builder()
                .reply(allReply)
                .build();
    }

    public ShortsReplyListResponseDTO insertShortsReply(Long id, ShortsPostRequestDTO dto) {
        log.debug("쇼츠 댓글 저장 서비스 실행!");

        ShortsReply reply = dto.toEntity(id);

        shortsReplyRepository.save(reply);

        return retrieve(id);
    }

    public ShortsReplyListResponseDTO deleteShortsReply(Long shortsId,Long replyId) {

        try {
            shortsReplyRepository.deleteById(replyId);

            return retrieve(shortsId);
        } catch (Exception e) {
            log.error("삭제에 실패했습니다. - ID: {}, Error: {}", replyId, e.getMessage());
            throw new RuntimeException("해당 아이디를 가진 댓글이 없습니다!");
        }
    }

    public ShortsReplyListResponseDTO updateReply(ShortsUpdateRequestDTO dto) {
        Optional<ShortsReply> target = shortsReplyRepository.findById(dto.getReplyId());

        target.ifPresent(reply -> {
            reply.setContext(dto.getContext());
            reply.setModify(reply.getModify()+1);
            shortsReplyRepository.save(reply);
        });


        return retrieve(dto.getShortsId());
    }
}
