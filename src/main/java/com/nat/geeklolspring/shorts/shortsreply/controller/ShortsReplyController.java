package com.nat.geeklolspring.shorts.shortsreply.controller;

import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyListResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.service.ShortsReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
//@CrossOrigin(origins = {"http://localhost:3000","",""})
@RequestMapping("/api/shorts/reply")
public class ShortsReplyController {
    private final ShortsReplyService shortsReplyService;

    @GetMapping("/{id}")
    public ResponseEntity<?> replyList(@PathVariable Long id) {
        log.info("/api/shorts/reply/{} : Get!", id);

        try {
            ShortsReplyListResponseDTO replyList = shortsReplyService.retrieve(id);

            if(replyList.getReply().isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(ShortsReplyListResponseDTO
                                .builder()
                                .error("아직 댓글이 없습니다.")
                                .build());
            }

            return ResponseEntity.ok().body(replyList);
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(ShortsReplyListResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addReply(
            @PathVariable Long id,
            @RequestBody ShortsPostRequestDTO dto) {

        log.info("api/shorts/reply/{} : Post!", id);
        log.warn("전달받은 데이터 : {}", dto);

        try {
            if (dto == null)
                throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");

            ShortsReplyListResponseDTO replyList = shortsReplyService.insertShortsReply(id, dto);
            return ResponseEntity.ok().body(replyList);

        } catch (DTONotFoundException e) {
            log.warn("필요한 정보를 전달받지 못했습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
