package com.nat.geeklolspring.troll.apply.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyDeleteRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplySearchRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.RulingApplyRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyDetailResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.apply.service.RulingApplyService;
import com.nat.geeklolspring.utils.files.Videos;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor

@RequestMapping("/troll/apply")
public class RulingApplyController {
    private final RulingApplyService rulingApplyService;

    //게시물 목록 전체 조회
    @GetMapping
    public ResponseEntity<?> findAllBoard(
            @PageableDefault(page = 1, size = 10) Pageable pageInfo,
            @RequestParam(name = "type", required = false) String orderType
    ){
        log.info("트롤 지원 조회 실행");
        log.info("{}",orderType);
        try {
            RulingApplyResponseDTO applyBoardList = rulingApplyService.findAllBoard(pageInfo,orderType);
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
            @RequestPart(value = "boardFile") MultipartFile boardFile,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        log.info("트롤 지원 만들기 실행");

        try {
            RulingApplyDetailResponseDTO applyBoard = rulingApplyService.createBoard(dto, userInfo, boardFile);
            return ResponseEntity.ok().body(applyBoard);
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
            @PathVariable Long applyId
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
            @RequestBody ApplyDeleteRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        try {
            rulingApplyService.deleteBoard(userInfo,dto);
            return ResponseEntity.ok().body(1);
        }catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(RulingApplyResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    // 게시판 검색하기
    @PostMapping("/search")
    public ResponseEntity<?> searchBoard(
            @PageableDefault(page = 1, size = 10) Pageable pageInfo,
            @RequestBody ApplySearchRequestDTO dto
            ){
        try {
            RulingApplyResponseDTO applyList = rulingApplyService.serchToBoard(dto, pageInfo);
            return ResponseEntity.ok().body(applyList);
        }catch (Exception e){
            return ResponseEntity
                    .internalServerError()
                    .body(RulingApplyResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    //비디오인지 체크하는 패치
    @PostMapping("/check-video")
    public ResponseEntity<?> checkVideo(
            @RequestPart(value = "boardFile") MultipartFile boardFile
    ){
        int i = rulingApplyService.CheckFile(boardFile);
        return ResponseEntity.ok().body(i);
    }

    //비디오 파일 불러오기
    @GetMapping("/load-video")
    public ResponseEntity<?> loadVideo(
            @RequestParam Long applyId
    ){

        try {
            String videoPath = rulingApplyService.getVideoPath(applyId);

            File videoFile = new File(videoPath);

            if (!videoFile.exists()) return ResponseEntity.notFound().build();

            byte[] fileData = FileCopyUtils.copyToByteArray(videoFile);

            HttpHeaders headers = new HttpHeaders();


            MediaType mediaType = Videos.extractFileExtension(videoPath);
            if (mediaType == null){
                return ResponseEntity.internalServerError().body("비디오가 아닙니다");
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

    //로그인한 사람 게시물 조회하기
    @GetMapping("/my")
    public ResponseEntity<?> findMyBoard(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("나의 페이지 트롤 지원 조회 실행");
        try {
            RulingApplyResponseDTO applyBoardList = rulingApplyService.findByUserId(userInfo,pageInfo);
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
