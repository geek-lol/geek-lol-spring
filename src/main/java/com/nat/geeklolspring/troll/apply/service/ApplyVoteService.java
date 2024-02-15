package com.nat.geeklolspring.troll.apply.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.entity.VoteApply;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyVotePostRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.ApplyVoteResponseDTO;
import com.nat.geeklolspring.troll.apply.repository.ApplyVoteCheckRepository;
import com.nat.geeklolspring.troll.apply.repository.RulingApplyRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class ApplyVoteService {
    private final ApplyVoteCheckRepository voteCheckRepository;
    private final RulingApplyService rulingApplyService;
    private final RulingApplyRepository rulingApplyRepository;
    private final UserRepository userRepository;


    public ApplyVoteResponseDTO createVote(ApplyVotePostRequestDTO dto, TokenUserInfo userInfo) {
        log.debug("좋아요 저장 서비스 실행!");

        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        // 필요한 정보가 잘 입력되어 있는지 확인
        if (dto.getApplyId() == null) {
            throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");
        }

        VoteApply entity = VoteApply.builder()
                .receiver(user)
                .applyId(rulingApplyRepository.findById(dto.getApplyId()).orElseThrow())
                .build();
        log.info("voteApplt entity:{}",entity);
        // 좋아요 등록
        VoteApply saved = voteCheckRepository.save(entity);
        rulingApplyRepository.plusUpCount(saved.getApplyId().getApplyId());

        log.info("좋아요 정보 저장 성공! 정보 : {}", saved);

        return new ApplyVoteResponseDTO(saved);
    }

    public ApplyVoteResponseDTO getVote(long applyId, TokenUserInfo userInfo) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        BoardApply boardApply = rulingApplyRepository.findById(applyId).orElseThrow();
        VoteApply voteCheck = voteCheckRepository.findByApplyIdAndReceiver(boardApply, user);

        // 해당 동영상에 대한 나의 좋아요 정보가 없다면 null을 리턴
        if(voteCheck == null) {
            log.warn("vote 정보가 없습니다.");
            return null;
        }
        else
            return ApplyVoteResponseDTO.builder()
                    .up(voteCheck.getUp())
                    .total(boardApply.getUpCount())
                    .build();

    }

    public ApplyVoteResponseDTO changeVote(VoteApply vote) {
        BoardApply boardApply = rulingApplyRepository.findById(vote.getVoteId()).orElseThrow();
        // vote 값 수정
        if (vote.getUp() == 1) {
            rulingApplyRepository.plusUpCount(boardApply.getApplyId());
            vote.setUp(0);
        }
        else{
            rulingApplyRepository.downUpCount(boardApply.getApplyId());
            vote.setUp(1);
        }

        // 수정한 vote 값 DB에 저장
        VoteApply saved = voteCheckRepository.save(vote);

        // 수정된 정보를 저장해서 Controller에 전달
        return ApplyVoteResponseDTO.builder()
                .up(saved.getUp())
                .total(boardApply.getUpCount())
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
        BoardApply boardApply = rulingApplyRepository.findById(applyId).orElseThrow();
        User user = userRepository.findById(accountId).orElseThrow();
        // 쇼츠 아이디와 계정명이 일치하는 vote정보를 리턴
        return voteCheckRepository.findByApplyIdAndReceiver(boardApply, user);
    }
}
