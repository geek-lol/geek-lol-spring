package com.nat.geeklolspring.troll.ruling.controller;

import com.nat.geeklolspring.troll.ruling.dto.response.CurrentBoardListResponseDTO;
import com.nat.geeklolspring.troll.ruling.service.RulingBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/troll/ruling/board")
public class RulingBoardController {
    private final RulingBoardService rulingBoardService;

    // 최근 투표게시물 2개 반환
    @GetMapping
    public ResponseEntity<?> findBoardTwo(){
        log.info("/troll/ruling/board/ !! ");
        try {
            CurrentBoardListResponseDTO currentBoardListResponseDTO = rulingBoardService.descRulingBoard();
            return ResponseEntity.ok().body(currentBoardListResponseDTO);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
