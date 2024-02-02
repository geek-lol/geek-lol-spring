package com.nat.geeklolspring.board.vote.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.vote.dto.request.VotePostRequestDTO;
import com.nat.geeklolspring.board.vote.dto.response.VoteResponseDTO;
import com.nat.geeklolspring.board.vote.repository.VoteCheckRepository;
import com.nat.geeklolspring.entity.VoteCheck;
import com.nat.geeklolspring.exception.DTONotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {
    private final VoteCheckRepository voteCheckRepository;

    public VoteResponseDTO createVote(VotePostRequestDTO dto, TokenUserInfo userInfo) {
        log.debug("좋아요 저장 서비스 실행!");

        // 필요한 정보가 잘 입력되어 있는지 확인
        if (dto.getShortsId() == null) {
            throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");
        }

        VoteCheck entity = dto.toEntity(dto);
        entity.setReceiver(userInfo.getUserId());

        // 좋아요 등록
        VoteCheck saved = voteCheckRepository.save(entity);

        log.info("좋아요 정보 저장 성공! 정보 : {}", saved);

        return new VoteResponseDTO(saved);
    }

    public VoteResponseDTO getVote(long shortsId, TokenUserInfo userInfo) {
        String userId = userInfo.getUserId();

        VoteCheck voteCheck = voteCheckRepository.findByShortsIdAndReceiver(shortsId, userId);

        // 해당 동영상에 대한 나의 좋아요 정보가 없다면 null을 리턴
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
        // vote 값 수정
        if (vote.getUp() == 1)
            vote.setUp(0);
        else
            vote.setUp(1);

        // 수정한 vote 값 DB에 저장
        VoteCheck saved = voteCheckRepository.save(vote);

        // 수정된 정보를 저장해서 Controller에 전달
        return VoteResponseDTO.builder()
                .up(saved.getUp())
                .build();
    }

    public boolean VoteCheck(VotePostRequestDTO dto, TokenUserInfo userInfo) {
        VoteCheck vote = findVote(dto.getShortsId(), userInfo.getUserId());

        log.info("vote : {}", vote);

        if (vote != null)
            return true;
        else
            return false;
    }

    public VoteCheck findVote(long shortsId, String accountId) {
        // 쇼츠 아이디와 계정명이 일치하는 vote정보를 리턴
        return voteCheckRepository.findByShortsIdAndReceiver(shortsId, accountId);
    }
}
