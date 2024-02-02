package com.nat.geeklolspring.troll.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinResponseDTO;
import com.nat.geeklolspring.troll.dto.request.RulingApplyRequestDTO;
import com.nat.geeklolspring.troll.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.service.RulingApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor

@RequestMapping("/troll/support")
public class RulingApplyController {
    private final RulingApplyService rulingApplyService;

    //게시물 목록 전체 조회
    @GetMapping
    public ResponseEntity<?> findAllBoard(){
        log.info("트롤 지원 조회 실행");
        try {
            RulingApplyResponseDTO applyBoardList = rulingApplyService.findAllBoard();
            return ResponseEntity.ok().body(applyBoardList);
        }catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(RulingApplyResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    // 게시물 등록
    @PostMapping
    public ResponseEntity<?> createBoard(
            @RequestBody RulingApplyRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        log.info("트롤 지원 만들기 실행");

        try {
            RulingApplyResponseDTO applyBoardList = rulingApplyService.createBoard(dto, userInfo);
            return ResponseEntity.ok().body(applyBoardList);
        }catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(RulingApplyResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    //게시물 삭제
    @DeleteMapping
    public ResponseEntity<?> deleteBoard(
            @Validated Long boardId,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        try {
            RulingApplyResponseDTO applyBoardList = rulingApplyService.deleteBoard(userInfo, boardId);
            return ResponseEntity.ok().body(applyBoardList);
        }catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(RulingApplyResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }


    }
}
