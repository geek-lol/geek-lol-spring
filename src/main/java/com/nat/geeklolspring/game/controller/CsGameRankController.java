package com.nat.geeklolspring.game.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.game.dto.request.GameRankRequestDTO;
import com.nat.geeklolspring.game.dto.response.GameRankListResponseDTO;
import com.nat.geeklolspring.game.service.CsGameRankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor

@RequestMapping("/game/cs")
public class CsGameRankController {
    private final CsGameRankService csGameRankService;

    @GetMapping
    public ResponseEntity<?> findRanking(){
        log.info("/game/cs : 막타 게임 랭킹 조회! ");
        try{
            GameRankListResponseDTO rank = csGameRankService.findRank();
            return ResponseEntity.ok().body(rank);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(GameRankListResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }
    @PostMapping
    public ResponseEntity<?> addRanking(
            @RequestBody GameRankRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo
            ){
        log.info("/game/res : 막타 게임 랭킹 추가! ");
        try{
            GameRankListResponseDTO rank = csGameRankService.addRank(dto,userInfo);
            return ResponseEntity.ok().body(rank);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(GameRankListResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }
}
