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
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
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
    public ResponseEntity<?> boardList(
            @PageableDefault(page = 1, size = 20) Pageable pageInfo,
            @Validated @RequestParam(name = "title",required = false) String titleKeyword,
            @Validated @RequestParam(name = "poster",required = false) String posterKeyword,
            @Validated @RequestParam(name = "content",required = false) String contentKeyword
    ) {
        log.info("/board/bulletin : Get!");

        log.info("title : {}",titleKeyword);
        log.info("poster : {}",posterKeyword);
        log.info("contentKeyword : {}",contentKeyword);

        try {
            BoardBulletinResponseDTO boardBulletinList = boardBulletinService.retrieve(titleKeyword,posterKeyword,contentKeyword,pageInfo);
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
            @Validated String bulletinId
    ) {
        log.info("/board/bulletin/detail : Get!");

        log.info("bulletinId : {}",bulletinId);

        try {
            BoardBulletinDetailResponseDTO boardBulletin = boardBulletinService.detailRetrieve(bulletinId);
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
            @Validated @RequestPart("boardInfo")BoardBulletinWriteRequestDTO dto, // 생성시 받을 정보
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
//        Map<String, String> fileMap = FileUtil.uploadFile(fileUrl, rootFilePath);
//        String filePath = fileMap.get("filePath");

        try {
            String uploadImagePath = null;
            if (fileUrl != null) {
                log.info("file-name: {}",fileUrl.getOriginalFilename());
                uploadImagePath = boardBulletinService.uploadImage(fileUrl);
            }
            BoardBulletinDetailResponseDTO responseDTO =
                    boardBulletinService.create(dto,userInfo,uploadImagePath);
            return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalStateException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
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

//        if (!dto.getPosterId().equals(userInfo.getUserId()) || !userInfo.getRole().toString().equals("ADMIN")){
//            return ResponseEntity.badRequest().body("수정권한이 없습니다");
//        }

//        Map<String, String> fileMap = FileUtil.uploadFile(fileUrl, rootFilePath);
//        String filePath = fileMap.get("filePath");

        try {
            String uploadImagePath = null;
            if (fileUrl != null) {
                log.info("file-name: {}",fileUrl.getOriginalFilename());
                uploadImagePath = boardBulletinService.uploadImage(fileUrl);
            }
            BoardBulletinDetailResponseDTO responseDTO =
                    boardBulletinService.modify(dto,uploadImagePath);
            return ResponseEntity.ok().body(responseDTO);
        } catch (IllegalStateException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
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

    //이미지 파일 불러오기
    @GetMapping("/load-profile")
    public ResponseEntity<?> loadProfile(
            @Validated String bulletinId
    ){

        try {
            String imagePath = boardBulletinService.getImagePath(Long.valueOf(bulletinId));

            File file = new File(imagePath);

            if (!file.exists()) return ResponseEntity.notFound().build();

            byte[] fileData = FileCopyUtils.copyToByteArray(file);

            HttpHeaders headers = new HttpHeaders();


            MediaType mediaType = extractFileExtension(imagePath);
            if (mediaType == null){
                return ResponseEntity.internalServerError().body("이미지가 아닙니다");
            }

            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }


    private MediaType extractFileExtension(String filePath){

        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        switch (ext.toUpperCase()) {
            case "JPEG": case "JPG":
                return MediaType.IMAGE_JPEG;
            case "PNG":
                return MediaType.IMAGE_PNG;
            case "GIF":
                return MediaType.IMAGE_GIF;
            default:
                return null;
        }

    }




}
