package com.nat.geeklolspring.game.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.game.dto.request.GameRankRequestDTO;
import com.nat.geeklolspring.game.dto.response.GameRankListResponseDTO;
import com.nat.geeklolspring.game.service.ResGameRankService;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor

@RequestMapping("/game/res")
public class ResGameRankController {
    private final ResGameRankService resGameRankService;

    @GetMapping
    public ResponseEntity<?> findRanking(){
        log.info("/game/res : 반응속도 게임 랭킹 조회! ");
        try{
            GameRankListResponseDTO rank = resGameRankService.findRank();
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
        log.info("/game/res : 반응속도 게임 랭킹 추가! ");
        try{
            GameRankListResponseDTO rank = resGameRankService.addRank(dto,userInfo);
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
