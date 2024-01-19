package com.nat.geeklolspring.vote.service;

import com.nat.geeklolspring.exception.NoUserInfoFoundException;
import com.nat.geeklolspring.vote.dto.request.VotePostRequestDTO;
import com.nat.geeklolspring.vote.dto.response.VoteResponseDTO;
import com.nat.geeklolspring.entity.VoteCheck;
import com.nat.geeklolspring.vote.repository.VoteCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {
    private final VoteCheckRepository voteCheckRepository;

    public VoteResponseDTO create(VotePostRequestDTO dto) {
        log.debug("vote save service execute!");

        if (dto == null) {
            throw new NoUserInfoFoundException("정보가 입력되지 않았습니다.");
        }

        VoteCheck saved = voteCheckRepository.save(dto.toEntity(dto));

        log.info("좋아요 / 싫어요 정보 저장 성공! 정보 : {}", saved);

        return new VoteResponseDTO(saved);
    }
}
