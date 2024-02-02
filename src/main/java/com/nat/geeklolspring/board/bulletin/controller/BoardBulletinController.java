package com.nat.geeklolspring.board.bulletin.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinModifyRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinWriteRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDeleteResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinResponseDTO;
import com.nat.geeklolspring.board.bulletin.service.BoardBulletinService;
import com.nat.geeklolspring.utils.upload.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

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
    public ResponseEntity<?> boardCreate( // 생성
            @AuthenticationPrincipal TokenUserInfo userInfo, // 토큰에서 주는 유저 정보
            @RequestPart("boardInfo")BoardBulletinWriteRequestDTO dto, // 생성시 받을 정보
            @RequestPart(value = "fileUrl",required = false) MultipartFile fileUrl, // 게시판 글 내에 파일
            BindingResult result
            ){
        log.info("/board/bulletin POST - {}, {}",dto,fileUrl);

        if (result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }

        Map<String, String> fileMap = FileUtil.uploadFile(fileUrl, rootFilePath);
        String filePath = fileMap.get("filePath");

        try {
            BoardBulletinDetailResponseDTO responseDTO =
                    boardBulletinService.create(dto,userInfo,filePath);
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            log.warn("문제 발생");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/modify")
    public ResponseEntity<?> modify(
            @AuthenticationPrincipal TokenUserInfo userInfo, // 토큰에서 주는 유저 정보
            @RequestPart("boardInfo") BoardBulletinModifyRequestDTO dto, // 수정시 받을 정보
            @RequestPart(value = "fileUrl",required = false) MultipartFile fileUrl, // 게시판 글 내에 파일
            BindingResult result
    ){
        log.info("/board/bulletin/modify POST - {}, {}",dto,fileUrl);

        log.info("userinfo ID : {}",userInfo.getUserId());
        log.info("dto posterId : {}",dto.getPosterId());
//
//        if (!dto.getPosterId().equals(userInfo.getUserId()) || !userInfo.getRole().toString().equals("ADMIN")){
//            return ResponseEntity.badRequest().body("수정권한이 없습니다");
//        }


        Map<String, String> fileMap = FileUtil.uploadFile(fileUrl, rootFilePath);
        String filePath = fileMap.get("filePath");

        try {
            BoardBulletinDetailResponseDTO responseDTO =
                    boardBulletinService.modify(dto,filePath);
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            log.warn("문제 발생");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping()
    public void boarddelete( // 삭제
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
