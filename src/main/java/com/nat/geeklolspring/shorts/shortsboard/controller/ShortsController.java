package com.nat.geeklolspring.shorts.shortsboard.controller;

import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.exception.IdNotFoundException;
import com.nat.geeklolspring.shorts.shortsboard.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.service.ShortsService;
import com.nat.geeklolspring.utils.upload.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000","",""})
@RequestMapping("/api/shorts")
public class ShortsController {
    // 업로드한 shorts를 저장할 로컬 위치
    @Value("D:/geek-lol/upload/shorts/video")
    private String rootShortsPath;

    @Value("D/geek-lol/upload/shorts/thumbnail")
    private String rootThumbnailPath;

    private final ShortsService shortsService;

    @GetMapping()
    public ResponseEntity<?> shortsList() {
        log.info("/api/shorts : Get!");

        try {
            ShortsListResponseDTO shortsList = shortsService.retrieve();

            if(shortsList.getShorts().isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(ShortsListResponseDTO
                                .builder()
                                .error("아직 업로드된 동영상이 없습니다!")
                                .build());
            }

            return ResponseEntity.ok().body(shortsList);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(ShortsListResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    // 쇼츠 생성
    @PostMapping()
    public ResponseEntity<?> addShorts(
            @RequestPart ShortsPostRequestDTO dto,
            @RequestPart("videoUrl") MultipartFile fileUrl,
            @RequestPart("thumbnail")MultipartFile thumbnail,
            BindingResult result
    ) {
        // 입력값 검증에 걸리면 400번 코드와 함께 메시지를 클라이언트에 전송
        if(result.hasErrors()) {
            return ResponseEntity
                    .badRequest()
                    .body(result.toString())
                    ;
        }

        log.info("/api/shorts : POST");
        log.warn("request parameter : {}", dto);

        dto.setVideoLink(fileUrl);
        dto.setVideoThumbnail(thumbnail);

        try {
            if (dto.getTitle().isEmpty() || dto.getVideoLink().isEmpty() || dto.getVideoThumbnail().isEmpty() || dto.getUploaderId().isEmpty())
                throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");

            Map<String, String> videoMap = FileUtil.uploadVideo(fileUrl, thumbnail, rootShortsPath, rootThumbnailPath);
            String videoPath = videoMap.get("videoPath");
            String thumbnailPath = videoMap.get("thumbnailPath");

            ShortsListResponseDTO shortsList = shortsService.insertVideo(dto, videoPath, thumbnailPath);
            return ResponseEntity.ok().body(shortsList);

        } catch (DTONotFoundException e) {
            log.warn("필요한 정보를 전달받지 못했습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 쇼츠 삭제 요청
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShorts(@PathVariable Long id) {

        log.info("/api/shorts/{} DELETE !!", id);

        if(id == null) {
            return ResponseEntity
                    .badRequest()
                    .body(ShortsListResponseDTO
                            .builder()
                            .error("ID값을 보내주세요!")
                            .build());
        }

        try {
            ShortsListResponseDTO shortsList = shortsService.deleteShorts(id);
            return ResponseEntity.ok().body(shortsList);
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ShortsListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        }
    }
}
