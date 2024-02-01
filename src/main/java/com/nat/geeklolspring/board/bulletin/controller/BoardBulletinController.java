package com.nat.geeklolspring.board.bulletin.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinWriteRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDeleteResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinResponseDTO;
import com.nat.geeklolspring.board.bulletin.service.BoardBulletinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/board/bulletin")
public class BoardBulletinController {

    @Value("D:/geek-lol/upload/file")
    private String rootFilePath;

    private final BoardBulletinService boardBulletinService;

    @GetMapping()
    public ResponseEntity<?> boardList() {
        log.info("/board/bulletin : Get!");

        try {
            BoardBulletinResponseDTO boardBulletinList =boardBulletinService.retrieve();
            return ResponseEntity.ok().body(boardBulletinList);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(BoardBulletinResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<?> boardDetail(
            @RequestBody String bulletinId
    ) {
        log.info("/board/bulletin/detail : Get!");

        try {
            BoardBulletinDetailResponseDTO boardBulletin =boardBulletinService.detailRetrieve(Long.valueOf(bulletinId));
            return ResponseEntity.ok().body(boardBulletin);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(BoardBulletinResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<?> boardCreate(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @RequestPart("boardInfo")BoardBulletinWriteRequestDTO dto,
            @RequestPart("fileUrl") MultipartFile fileUrl,
            BindingResult result
            ){
        log.info("/board/bulletin POST - {}, {}",dto,fileUrl);

        if (result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }

        try {
            BoardBulletinDetailResponseDTO responseDTO =  boardBulletinService.create(dto,userInfo,fileUrl.toString());
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            log.warn("문제 발생");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping()
    public void boarddelete(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestBody BoardBulletinDeleteResponseDTO dto
    ){
        try {
            boardBulletinService.delete(userInfo,dto);
            log.info("delete board : {}",dto.getTitle());
        }catch (RuntimeException e){
            log.warn(e.getMessage());
        }

    }




}
