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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardVoteService {
    private final BoardVoteCheckRepository voteCheckRepository;
    private final BoardBulletinRepository boardBulletinRepository;
    private final UserRepository userRepository;

    public BoardVoteResponseDTO getVote(Long boardId, TokenUserInfo userInfo) {
        String userId = userInfo.getUserId();

        Optional<BoardBulletin> boardBulletin = boardBulletinRepository.findById(boardId);
        Stream<BoardBulletin> bulletin = boardBulletin.stream();

        Optional<User> userOptional = userRepository.findById(userId);
        Stream<User> user = userOptional.stream();

        BulletinCheck voteCheck = voteCheckRepository.findByBoardBulletinIdAndBulletinCheckerId((BoardBulletin) bulletin, (User) user);

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

    public BoardVoteResponseDTO createVote(BoardVotePostRequestDTO dto, TokenUserInfo userInfo) {
        log.debug("좋아요 저장 서비스 실행!");

        // 필요한 정보가 잘 입력되어 있는지 확인
        if (dto.getBoardId() == null) {
            throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");
        }

        BulletinCheck entity = dto.toEntity(dto);

        Optional<User> userOptional = userRepository.findById(userInfo.getUserId());

        Stream<User> user = userOptional.stream();

        entity.setBulletinCheckerId((User) user);

        // 좋아요 등록
        BulletinCheck saved = voteCheckRepository.save(entity);
        // 좋아요 등록에 따른 좋아요 카운트 증가
        boardBulletinRepository.plusUpCount(dto.getBoardId().getBulletinId());

        log.info("좋아요 정보 저장 성공! 정보 : {}", saved);

        return new BoardVoteResponseDTO(saved);
    }

    public BoardVoteResponseDTO changeVote(BulletinCheck vote) {
        // vote 값 수정
        if (vote.getGood() == 1) {
            // vote 값 수정에 따른 해당 쇼츠의 좋아요 수 감소
            boardBulletinRepository.downUpCount(vote.getBoardBulletinId().getBulletinId());
            vote.setGood(0);
        }
        else {
            // vote 값 수정에 따른 해당 쇼츠의 좋아요 수 증가
            boardBulletinRepository.plusUpCount(vote.getBoardBulletinId().getBulletinId());
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
        BulletinCheck vote = findVote(dto.getBoardId().getBulletinId(), userInfo.getUserId());

        log.info("vote : {}", vote);

        if (vote != null)
            return true;
        else
            return false;
    }

    public BulletinCheck findVote(Long bulletinId, String accountId) {


        Optional<BoardBulletin> boardBulletin = boardBulletinRepository.findById(bulletinId);
        Stream<BoardBulletin> bulletin = boardBulletin.stream();

        Optional<User> userOptional = userRepository.findById(accountId);
        Stream<User> user = userOptional.stream();

        // 쇼츠 아이디와 계정명이 일치하는 vote정보를 리턴
        return voteCheckRepository.findByBoardBulletinIdAndBulletinCheckerId((BoardBulletin) bulletin, (User) user);
    }
}
