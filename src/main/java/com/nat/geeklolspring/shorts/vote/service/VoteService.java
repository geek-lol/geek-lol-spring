package com.nat.geeklolspring.shorts.vote.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.shorts.shortsboard.repository.ShortsRepository;
import com.nat.geeklolspring.shorts.vote.dto.request.VotePostRequestDTO;
import com.nat.geeklolspring.shorts.vote.dto.response.VoteResponseDTO;
import com.nat.geeklolspring.entity.VoteCheck;
import com.nat.geeklolspring.shorts.vote.repository.VoteCheckRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VoteService {
    private final VoteCheckRepository voteCheckRepository;
    private final ShortsRepository shortsRepository;
    private final UserRepository userRepository;
    public VoteResponseDTO getVote(long shortsId, TokenUserInfo userInfo) {
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        BoardShorts shorts = shortsRepository.findByShortsId(shortsId);

        VoteCheck voteCheck = voteCheckRepository.findByShortsAndUser(shorts,user);

        // 해당 동영상에 대한 나의 좋아요 정보가 없다면 null을 리턴
        if(voteCheck == null) {
            log.warn("vote 정보가 없습니다.");
            return null;
        }
        else
            return VoteResponseDTO.builder()
                    .up(voteCheck.getUp())
                    .total(shorts.getUpCount())
                    .build();

    }

    public VoteResponseDTO createVote(VotePostRequestDTO dto, TokenUserInfo userInfo) {
        log.debug("좋아요 저장 서비스 실행!");
        BoardShorts shorts = shortsRepository.findByShortsId(dto.getShortsId());
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        // 필요한 정보가 잘 입력되어 있는지 확인
        if (shorts == null || user == null) {
            throw new DTONotFoundException("shortsId or userInfo를 확인하세요 ");
        }

        VoteCheck entity = dto.toEntity(shorts,user);
        log.error("쇼츠 좋아요 저장될 엔터티 : {},{}",entity.getShorts(),entity.getUser());
        // 좋아요 등록
        VoteCheck saved = voteCheckRepository.save(entity);

        BoardShorts shorts = shortsRepository.findByShortsId(dto.getShortsId());
        int i = shorts.getUpCount();
        log.info("좋아요 정보 저장 성공! 정보 : {}", saved);

        return new VoteResponseDTO(saved,i);
    }

    public VoteResponseDTO changeVote(VoteCheck vote) {
        BoardShorts shorts = shortsRepository.findByShortsId(vote.getShorts().getShortsId());
        // vote 값 수정
        if (vote.getUp() == 1) {
            // vote 값 수정에 따른 해당 쇼츠의 좋아요 수 증가
            shortsRepository.plusUpCount(vote.getShorts().getShortsId());
            vote.setUp(0);
        }
        else {
            // vote 값 수정에 따른 해당 쇼츠의 좋아요 수 감소
            shortsRepository.downUpCount(vote.getShorts().getShortsId());
            vote.setUp(1);
        }

        BoardShorts shorts = shortsRepository.findByShortsId(vote.getShortsId());
        int i = shorts.getUpCount();

        // 수정한 vote 값 DB에 저장
        VoteCheck saved = voteCheckRepository.save(vote);

        // 수정된 정보를 저장해서 Controller에 전달
        return VoteResponseDTO.builder()
                .up(saved.getUp())
                .total(shorts.getUpCount())
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
        BoardShorts shorts = shortsRepository.findByShortsId(shortsId);
        User user = userRepository.findById(accountId).orElseThrow();
        // 쇼츠 아이디와 계정명이 일치하는 vote정보를 리턴
        return voteCheckRepository.findByShortsAndUser(shorts,user);
    }
}
