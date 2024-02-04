package com.nat.geeklolspring.troll.apply.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.troll.apply.dto.request.RulingApplyRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyDetailResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.apply.service.RulingApplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor

@RequestMapping("/troll/apply")
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
            @Validated @RequestPart("board") RulingApplyRequestDTO dto,
            @RequestPart(value = "profileImage", required = false) MultipartFile boardFile,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        log.info("트롤 지원 만들기 실행");

        try {
            RulingApplyResponseDTO applyBoardList = rulingApplyService.createBoard(dto, userInfo, boardFile);
            return ResponseEntity.ok().body(applyBoardList);
        }catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(RulingApplyResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    //게시물 상세보기
    @GetMapping("/detail/{applyId}")
    public ResponseEntity<?> detailBoard(
          @RequestParam Long applyId
    ){
        log.info("지원 디테일 실행! {}",applyId);
        try {
            RulingApplyDetailResponseDTO applyBoard = rulingApplyService.detailBoard(applyId);
            return ResponseEntity.ok().body(applyBoard);
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
