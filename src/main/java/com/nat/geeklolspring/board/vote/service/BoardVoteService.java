package com.nat.geeklolspring.board.vote.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.bulletin.repository.BoardBulletinRepository;
import com.nat.geeklolspring.board.vote.dto.request.BoardVotePostRequestDTO;
import com.nat.geeklolspring.board.vote.dto.response.BoardVoteResponseDTO;
import com.nat.geeklolspring.board.vote.repository.BoardVoteCheckRepository;
import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BulletinCheck;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardVoteService {
    private final BoardVoteCheckRepository voteCheckRepository;
    private final BoardBulletinRepository boardBulletinRepository;
    private final UserRepository userRepository;

    public BoardVoteResponseDTO getVote(long boardId, TokenUserInfo userInfo) {
        BoardBulletin bulletin = boardBulletinRepository.findById(boardId).orElseThrow();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        BulletinCheck voteCheck = voteCheckRepository.findByBulletinAndUser(bulletin,user);

        // 해당 동영상에 대한 나의 좋아요 정보가 없다면 null을 리턴
        if(voteCheck == null) {
            log.warn("vote 정보가 없습니다.");
            return null;
        }
        else
            return BoardVoteResponseDTO.builder()
                    .up(voteCheck.getGood())
                    .build();

    }

    @Transactional
    public BoardVoteResponseDTO createVote(BoardVotePostRequestDTO dto, TokenUserInfo userInfo) {
        log.debug("좋아요 저장 서비스 실행!");

        // 필요한 정보가 잘 입력되어 있는지 확인
        if (dto.getBoardId() == null) {
            throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");
        }
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        BoardBulletin bulletin = boardBulletinRepository.findById(dto.getBoardId()).orElseThrow();

        BulletinCheck entity = dto.toEntity(bulletin,user);

        // 좋아요 등록
        BulletinCheck saved = voteCheckRepository.save(entity);

        log.info("좋아요 정보 저장 성공! 정보 : {}", saved);

        return new BoardVoteResponseDTO(saved);
    }

    @Transactional
    public BoardVoteResponseDTO changeVote(BulletinCheck vote) {
        BoardBulletin bulletin = boardBulletinRepository.findById(vote.getBulletin().getBulletinId()).orElseThrow();
        // vote 값 수정
        if (vote.getGood() == 1) {
            // vote 값 수정에 따른 해당 쇼츠의 좋아요 수 감소
            boardBulletinRepository.downUpCount(vote.getBulletin().getBulletinId());
            vote.setGood(0);
        }
        else {
            // vote 값 수정에 따른 해당 쇼츠의 좋아요 수 증가
            boardBulletinRepository.plusUpCount(vote.getBulletin().getBulletinId());
            vote.setGood(1);
        }

        // 수정한 vote 값 DB에 저장
        BulletinCheck saved = voteCheckRepository.save(vote);

        // 수정된 정보를 저장해서 Controller에 전달
        return BoardVoteResponseDTO.builder()
                .up(saved.getGood())
                .build();
    }

    public boolean VoteCheck(BoardVotePostRequestDTO dto, TokenUserInfo userInfo) {
        BulletinCheck vote = findVote(dto.getBoardId(), userInfo.getUserId());

        log.info("vote : {}", vote);

        if (vote != null)
            return true;
        else
            return false;
    }

    public BulletinCheck findVote(long bulletinId, String accountId) {
        BoardBulletin bulletin = boardBulletinRepository.findById(bulletinId).orElseThrow();
        User user = userRepository.findById(accountId).orElseThrow();
        // 쇼츠 아이디와 계정명이 일치하는 vote정보를 리턴
        return voteCheckRepository.findByBulletinAndUser(bulletin, user);
    }
}
