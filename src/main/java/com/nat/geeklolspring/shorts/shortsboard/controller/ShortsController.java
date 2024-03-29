package com.nat.geeklolspring.shorts.shortsboard.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsboard.dto.request.ShortsDeleteRequestDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.shortsboard.service.ShortsService;
import com.nat.geeklolspring.utils.upload.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    //@Value("D:/geek-lol/upload/shorts/video")
    //private String rootShortsPath;

    private final ShortsService shortsService;
    private final FileUtil fileUtil;

    // shorts 리스트 가져오기
    @GetMapping()
    public ResponseEntity<?> shortsList(
    ) {
        log.info("/api/shorts : Get!");

        try {
            // 쇼츠 목록 가져오기
            ShortsListResponseDTO shortsList = shortsService.retrieve();

            log.warn("shortsList: {}", shortsList);

            // 가져온 shortsList가 비어있을 경우 아직 업로드된 동영상이 없다는 뜻
            if(shortsList.getShorts().isEmpty()) {
                return ResponseEntity
                        .ok()
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

    // 쇼츠 등록
    @PostMapping()
    public ResponseEntity<?> addShorts(
            @RequestPart("videoInfo") ShortsPostRequestDTO dto,
            @RequestPart("videoUrl") MultipartFile fileUrl,
            @AuthenticationPrincipal TokenUserInfo userInfo,
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

        // 따로 가져온 파일들을 dto안에 세팅하기
        //dto.setVideoLink(fileUrl);

        try {
            // 필요한 정보를 전달받지 못하면 커스텀 에러인 DTONotFoundException 발생
            if (dto.getTitle().isEmpty() || fileUrl.isEmpty())
                throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");

            // 동영상과 섬네일 이미지를 가공해 로컬폴더에 저장하고 경로를 리턴받기
            // 동영상 가공
            Map<String, String> videoMap = fileUtil.uploadFile(fileUrl);
            String videoPath = videoMap.get("filePath");
            
            // dto와 파일경로를 DB에 저장하는 서비스 실행
            shortsService.insertVideo(dto, videoPath, userInfo);

            // return : 전달받은 파일들이 DB에 저장된 새 동영상 리스트들
            return ResponseEntity.ok().body(null);

        } catch (DTONotFoundException e) {
            log.warn("필요한 정보를 전달받지 못했습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 쇼츠 삭제 요청
    @DeleteMapping
    public ResponseEntity<?> deleteShorts(@RequestBody ShortsDeleteRequestDTO dto,
                                          @AuthenticationPrincipal TokenUserInfo userInfo
                                          ,@PageableDefault(page = 1, size = 10) Pageable pageInfo
    ) {

        log.info("/api/shorts/{} DELETE !!", dto);

        // 데이터를 전달받지 못했다면 실행
        if(dto == null) {
            return ResponseEntity
                    .badRequest()
                    .body(ShortsListResponseDTO
                            .builder()
                            .error("ID값을 보내주세요!")
                            .build());
        }

        try {
            // id에 해당하는 동영상을 지우는 서비스 실행
            // return : id에 해당하는 동영상이 삭제된 DB에서 동영상 리스트 새로 가져오기
            ShortsListResponseDTO DTO = shortsService.deleteShorts(dto, userInfo, pageInfo);

            ShortsListResponseDTO shortsList = shortsService.retrieve();

            return ResponseEntity.ok().body(shortsList);
        } catch (NotEqualTokenException e) {
          log.warn("작성자랑 일치하지 않는 사용자가 쇼츠 삭제를 시도했습니다!");
          return ResponseEntity
                  .badRequest()
                  .body(ShortsListResponseDTO
                          .builder()
                          .error("쇼츠 업로더만 삭제할 수 있습니다!")
                          .build());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ShortsListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        }
    }


    //동영상 파일 불러오기
    @GetMapping("/load-video/{shortsId}")
    public ResponseEntity<?> loadVideo(
            @PathVariable Long shortsId){
        log.error("shortsId : {}", shortsId);
        try {
            String shortPath = shortsService.getShortPath(shortsId);

            //File videoFile = new File(shortPath);
            //
            //if (!videoFile.exists()) return ResponseEntity.notFound().build();
            //
            //byte[] fileData = FileCopyUtils.copyToByteArray(videoFile);
            //
            //HttpHeaders headers = new HttpHeaders();
            //
            //
            //MediaType mediaType = Videos.extractFileExtension(shortPath);
            //if (mediaType == null){
            //    return ResponseEntity.internalServerError().body("비디오가 아닙니다");
            //}
            //
            //headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    //.headers(headers)
                    .body(shortPath);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }

    @GetMapping("/my")
    public ResponseEntity<?> myShorts(
            @PageableDefault(page = 1, size = 10) Pageable pageInfo,
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        try {
            ShortsListResponseDTO shortsListResponseDTO = shortsService.myUploadShort(userInfo,pageInfo);
            return ResponseEntity.ok().body(shortsListResponseDTO);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(ShortsListResponseDTO.builder()
                    .error(e.getMessage())
                    .build());
        }
    }

}
