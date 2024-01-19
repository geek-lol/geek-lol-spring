package com.nat.geeklolspring.vote.controller;

import com.nat.geeklolspring.exception.NoUserInfoFoundException;
import com.nat.geeklolspring.vote.dto.request.VotePostRequestDTO;
import com.nat.geeklolspring.vote.dto.response.VoteResponseDTO;
import com.nat.geeklolspring.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000","",""})
@RequestMapping("/api/vote")
public class VoteController {
    private final VoteService voteService;
    @GetMapping("/{shortsId}")
    public ResponseEntity<?> vote(
            @PathVariable long shortsId
            , @Validated @RequestParam String accountId
    ) {
        log.info("/api/vote/{}?accountId={} : GET!", shortsId, accountId);

        return null;
    }

    // 좋아요 / 싫어요 생성
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
            VoteResponseDTO voteResponseDTO = voteService.create(dto);
            return ResponseEntity.ok().body(voteResponseDTO);
        } catch (NoUserInfoFoundException e) {
            log.warn("유저 정보를 전달받지 못했습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
