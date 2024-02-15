package com.nat.geeklolspring.troll.ruling.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingVoteRequestDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.ProsAndConsDTO;
import com.nat.geeklolspring.troll.ruling.service.RulingVoteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor

@RequestMapping("/troll/ruling/vote")
public class RulingVoteController {
    private final RulingVoteService rulingVoteService;

    //투표 정보 조회
    @GetMapping("/{rulingId}")
    public ResponseEntity<?> voteStatus(
            @PathVariable Long rulingId
    ){
        ProsAndConsDTO prosAndConsDTO = rulingVoteService.prosAndCons(rulingId);
        return ResponseEntity.ok().body(prosAndConsDTO);
    }

    //투표 정보 저장
    //rulingId => 투표할 게시물 아이디
    //vote => 찬성투표시 vote=pros , 반대투표시 vote=cons
    @PostMapping
    public ResponseEntity<?> voteSave(
            @RequestBody RulingVoteRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        try
        {
            ProsAndConsDTO prosAndConsDTO = rulingVoteService.rulingVoteSave(dto, userInfo);
            if (prosAndConsDTO == null){
                return ResponseEntity.ok().body(ProsAndConsDTO.builder()
                        .error("이미 투표한 회원이거나 지난 투표게시물입니다")
                        .build());
            }
            return ResponseEntity.ok().body(prosAndConsDTO);
        }catch (NullPointerException e){
            throw new RuntimeException("토큰이 만료되었습니다.");
        }
    }
}
