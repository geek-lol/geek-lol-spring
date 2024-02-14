package com.nat.geeklolspring.board.vote.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.vote.dto.request.BoardVotePatchRequestDTO;
import com.nat.geeklolspring.board.vote.dto.request.BoardVotePostRequestDTO;
import com.nat.geeklolspring.board.vote.dto.response.BoardVoteResponseDTO;
import com.nat.geeklolspring.board.vote.service.BoardVoteService;
import com.nat.geeklolspring.entity.BulletinCheck;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.exception.DuplicatedVoteException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@Slf4j
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000","",""})
@RequestMapping("/board/vote")
public class BoardVoteController {
    private final BoardVoteService voteService;
    
    // 좋아요 정보 조회
    @GetMapping()
    public ResponseEntity<?> vote(
            @Validated @RequestParam Long bulletinId,
            @AuthenticationPrincipal TokenUserInfo userInfo
            ) {
        log.info("/api/vote?shortsId={} : GET!", bulletinId);

        // 해당 동영상에 대한 나의 좋아요 정보 가져오기
        // 정보가 없다면 null값을 리턴받음
        BoardVoteResponseDTO vote = voteService.getVote(bulletinId, userInfo);

        log.warn("vote : {}", vote);

        return ResponseEntity.ok().body(vote);
    }

    // 좋아요 생성
    @Transactional
    @PostMapping()
    public ResponseEntity<?> addVote(
            @Validated @RequestBody BoardVotePostRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            BindingResult result
    ) {
        // 입력값 검증에 걸리면 400번 코드와 함께 메시지를 클라이언트에 전송
        if(result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(result.toString())
                    ;
        }

        log.info("/board/vote : POST");
        log.warn("request parameter : {}", dto);

        try {
            // 해당 동영상에 대한 나의 좋아요 데이터가 있는지 확인
            // true : 좋아요 정보가 있음
            // false : 좋아요 정보가 없음
            boolean flag = voteService.VoteCheck(dto, userInfo);

            log.warn("flag : {}", flag);

            // 좋아요 정보가 없으면 실행
            if (!flag) {
                // 좋아요 정보 생성
                // 리턴은 생성된 좋아요 정보
                BoardVoteResponseDTO vote = voteService.createVote(dto, userInfo);
                return ResponseEntity.ok().body(vote);
            }

            // 좋아요 정보가 이미 있으므로 커스텀 에러인 DuplicatedVoteException 발생시키기
            throw new DuplicatedVoteException("이미 좋아요가 저장되어 있습니다!");

        } catch (DTONotFoundException e) {
            log.warn("유저 정보를 전달받지 못했습니다.");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (DuplicatedVoteException e) {
            log.warn("이미 좋아요 정보가 저장되어 있습니다.");
            return ResponseEntity
                    .badRequest()
                    .body(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }

    // 좋아요 상태 수정
    @Transactional
    @RequestMapping(method = {PUT, PATCH})
    public ResponseEntity<?> update(
            @Validated @RequestBody BoardVotePatchRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            HttpServletRequest request
    ) {
        log.info("/api/vote Request {}", request.getRequestURL());
        log.debug("dto: {}", dto);

        try {
            // 내 vote 정보 가져오는 서비스 실행
            BulletinCheck voteCheck = voteService.findVote(dto.getBoardId().getBulletinId(), userInfo.getUserId());
            // 내 vote 정보 수정하는 서비스 실행
            BoardVoteResponseDTO vote = voteService.changeVote(voteCheck);
            
            return ResponseEntity.ok().body(vote);
        } catch (Exception e) {
            log.warn("서버 에러가 발생했습니다! 사유 : {}", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }
}
