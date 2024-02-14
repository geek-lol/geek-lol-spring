package com.nat.geeklolspring.troll.apply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.vote.dto.response.BoardVoteResponseDTO;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.VoteApply;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyVotePostRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.ApplyVoteResponseDTO;
import com.nat.geeklolspring.troll.apply.repository.ApplyVoteCheckRepository;
import com.nat.geeklolspring.troll.apply.repository.RulingApplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApplyVoteService {
    private final ApplyVoteCheckRepository voteCheckRepository;
    private final RulingApplyRepository rulingApplyRepository;


    public ApplyVoteResponseDTO createVote(ApplyVotePostRequestDTO dto, TokenUserInfo userInfo) {
        Long applyId = dto.getApplyId();
        log.debug("좋아요 저장 서비스 실행!");

        // 필요한 정보가 잘 입력되어 있는지 확인
        if (dto.getApplyId() == null) {
            throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");
        }

        VoteApply entity = dto.toEntity(dto);
        entity.setReceiver(userInfo.getUserId());

        // 좋아요 등록
        VoteApply saved = voteCheckRepository.save(entity);
        rulingApplyRepository.plusUpCount(applyId);

        BoardApply boardApply = rulingApplyRepository.findById(dto.getApplyId()).orElseThrow();
        int i = boardApply.getUpCount();
        log.info("좋아요 정보 저장 성공! 정보 : {}", i);

        return new ApplyVoteResponseDTO(saved,i);
    }

    public ApplyVoteResponseDTO getVote(long applyId, TokenUserInfo userInfo) {
        String userId = userInfo.getUserId();

        VoteApply voteCheck = voteCheckRepository.findByApplyIdAndReceiver(applyId, userId);

        // 해당 동영상에 대한 나의 좋아요 정보가 없다면 null을 리턴
        if(voteCheck == null) {
            log.warn("vote 정보가 없습니다.");
            return null;
        }
        else
            return ApplyVoteResponseDTO.builder()
                    .up(voteCheck.getUp())
                    .build();

    }

    public ApplyVoteResponseDTO changeVote(VoteApply vote) {
        // vote 값 수정
        if (vote.getUp() == 1)
            vote.setUp(0);
        else
            vote.setUp(1);

        // 수정한 vote 값 DB에 저장
        VoteApply saved = voteCheckRepository.save(vote);

        // 수정된 정보를 저장해서 Controller에 전달
        return ApplyVoteResponseDTO.builder()
                .up(saved.getUp())
                .build();
    }

    public boolean VoteCheck(ApplyVotePostRequestDTO dto, TokenUserInfo userInfo) {
        VoteApply vote = findVote(dto.getApplyId(), userInfo.getUserId());

        log.info("vote : {}", vote);

        if (vote != null)
            return true;
        else
            return false;
    }

    public VoteApply findVote(long applyId, String accountId) {
        // 쇼츠 아이디와 계정명이 일치하는 vote정보를 리턴
        return voteCheckRepository.findByApplyIdAndReceiver(applyId, accountId);
    }
}
