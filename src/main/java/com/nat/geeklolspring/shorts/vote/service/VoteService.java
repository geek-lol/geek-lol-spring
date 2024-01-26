package com.nat.geeklolspring.shorts.vote.service;

import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.shorts.vote.dto.request.VotePostRequestDTO;
import com.nat.geeklolspring.shorts.vote.dto.response.VoteResponseDTO;
import com.nat.geeklolspring.entity.VoteCheck;
import com.nat.geeklolspring.shorts.vote.repository.VoteCheckRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {
    private final VoteCheckRepository voteCheckRepository;

    public VoteResponseDTO createVote(VotePostRequestDTO dto) {
        log.debug("좋아요 저장 서비스 실행!");

        if (dto == null) {
            throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");
        }

        VoteCheck saved = voteCheckRepository.save(dto.toEntity(dto));

        log.info("좋아요 정보 저장 성공! 정보 : {}", saved);

        return new VoteResponseDTO(saved);
    }

    public VoteCheck findVote(long shortsId, String accountId) {
        return voteCheckRepository.findByShortsIdAndReceiver(shortsId, accountId);
    }

    public VoteResponseDTO getVote(long shortsId, String accountId) {
        VoteCheck voteCheck = voteCheckRepository.findByShortsIdAndReceiver(shortsId, accountId);

        if(voteCheck == null) {
            log.warn("vote 정보가 없습니다.");
            return null;
        }
        else
            return VoteResponseDTO.builder()
                    .up(voteCheck.getUp())
                    .build();

    }

    public VoteResponseDTO changeVote(VoteCheck vote) {
        if (vote.getUp() == 1)
            vote.setUp(0);
        else
            vote.setUp(1);

        VoteCheck saved = voteCheckRepository.save(vote);

        return VoteResponseDTO.builder()
                .up(saved.getUp())
                .build();
    }

    public boolean VoteCheck(VotePostRequestDTO dto) {
        VoteCheck vote = findVote(dto.getShortsId(), dto.getReceiver());

        log.info("vote : {}", vote);

        if (vote != null)
            return true;
        else
            return false;
    }
}
