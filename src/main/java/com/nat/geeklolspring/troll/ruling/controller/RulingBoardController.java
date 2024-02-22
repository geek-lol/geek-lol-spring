package com.nat.geeklolspring.troll.ruling.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.troll.ruling.dto.response.CurrentBoardListResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingBoardDetailResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingBoardListResponseDTO;
import com.nat.geeklolspring.troll.ruling.service.RulingBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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

    // 투표게시물 전체 반환
    @GetMapping("/all")
    public ResponseEntity<?> findAllBoard(
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("/troll/ruling/board : 게시물 전체조회 실행");
        try {
            RulingBoardListResponseDTO allRulingBoard = rulingBoardService.findAllRulingBoard(pageInfo);
            return ResponseEntity.ok().body(allRulingBoard);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("서버 에러에용 놀라지마새용ㅎㅎ");
        }
    }

    // 투표 게시물 상세보기
    @GetMapping("/{rulingId}")
    public ResponseEntity<?> findDetailBoard(
            @Validated @PathVariable Long rulingId
    ){
        log.info("/troll/ruling/board/{} !!",rulingId);
        RulingBoardDetailResponseDTO detailBoard = rulingBoardService.findDetailBoard(rulingId);
        return ResponseEntity.ok().body(detailBoard);
    }

    //내 게시글 조회
    @GetMapping("/my")
    public ResponseEntity<?> findMyBoards(
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
            ,@AuthenticationPrincipal TokenUserInfo userInfo
            ){
        try {
            RulingBoardListResponseDTO myBoard = rulingBoardService.findMyBoard(pageInfo, userInfo);
            return ResponseEntity.ok().body(myBoard);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    RulingBoardListResponseDTO.builder()
                            .error(e.getMessage())
                            .build()
            );
        }
    }

    //비디오 파일 불러오기
    @GetMapping("/load-video/{rulingId}")
    public ResponseEntity<?> loadVideo(
            @PathVariable Long rulingId
    ){

        try {
            String videoPath = rulingBoardService.getVideoPath(rulingId);

            //File videoFile = new File(videoPath);
            //
            //if (!videoFile.exists()) return ResponseEntity.notFound().build();
            //
            //byte[] fileData = FileCopyUtils.copyToByteArray(videoFile);
            //
            //HttpHeaders headers = new HttpHeaders();
            //
            //
            //MediaType mediaType = Videos.extractFileExtension(videoPath);
            //if (mediaType == null){
            //    return ResponseEntity.internalServerError().body("비디오가 아닙니다");
            //}
            //
            //headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    //.headers(headers)
                    .body(videoPath);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

}
