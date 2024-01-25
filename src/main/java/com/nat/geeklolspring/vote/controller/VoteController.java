package com.nat.geeklolspring.vote.controller;

import com.nat.geeklolspring.entity.VoteCheck;
import com.nat.geeklolspring.exception.DuplicatedVoteException;
import com.nat.geeklolspring.exception.NoUserInfoFoundException;
import com.nat.geeklolspring.vote.dto.request.VotePatchRequestDTO;
import com.nat.geeklolspring.vote.dto.request.VotePostRequestDTO;
import com.nat.geeklolspring.vote.dto.response.VoteResponseDTO;
import com.nat.geeklolspring.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000","",""})
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService voteService;
    
    // 좋아요 정보 조회
    @GetMapping()
    public ResponseEntity<?> vote(
            @Validated @RequestParam Long shortsId
            , @Validated @RequestParam String accountId
    ) {
        log.info("/api/vote?shortsId={}&accountId={} : GET!", shortsId, accountId);

        VoteResponseDTO vote = voteService.getVote(shortsId, accountId);

        log.warn("vote : {}", vote);

        return ResponseEntity.ok().body(vote);
    }

    // 좋아요 생성
    @PostMapping()
    public ResponseEntity<?> addVote(
            @Validated @RequestBody VotePostRequestDTO dto,
            BindingResult result
            ) {
        // 입력값 검증에 걸리면 400번 코드와 함께 메시지를 클라이언트에 전송
        if(result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(result.toString())
                    ;
        }

        log.info("/api/vote : POST");
        log.warn("request parameter : {}", dto);

        try {
            boolean flag = voteService.VoteCheck(dto);

            log.warn("flag : {}", flag);

            if (!flag) {
                VoteResponseDTO vote = voteService.createVote(dto);
                return ResponseEntity.ok().body(vote);
            }

            throw new DuplicatedVoteException("이미 좋아요가 저장되어 있습니다!");

        } catch (NoUserInfoFoundException e) {
            log.warn("유저 정보를 전달받지 못했습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DuplicatedVoteException e) {
            log.warn("이미 좋아요 정보가 저장되어 있습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 좋아요 상태 수정
    @RequestMapping(method = {PUT, PATCH})
    public ResponseEntity<?> update(
            @Validated @RequestBody VotePatchRequestDTO dto
            , HttpServletRequest request
    ) {
        log.info("/api/vote Request {}", request.getRequestURL());
        log.debug("dto: {}", dto);

        try {
            VoteCheck voteCheck = voteService.findVote(dto.getShortsId(), dto.getReceiver());
            VoteResponseDTO vote = voteService.changeVote(voteCheck);
            return ResponseEntity.ok().body(vote);
        } catch (Exception e) {
            log.warn("서버 에러가 발생했습니다! 사유 : {}", e.getMessage());
            return ResponseEntity
                    .internalServerError()
                    .body(e.getMessage());
        }
    }
}
