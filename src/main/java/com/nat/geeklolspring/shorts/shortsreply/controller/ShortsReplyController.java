package com.nat.geeklolspring.shorts.shortsreply.controller;

import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.exception.IdNotFoundException;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
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

    @GetMapping("/{shortsId}")
    public ResponseEntity<?> replyList(@PathVariable Long shortsId) {
        log.info("/api/shorts/reply/{} : Get!", shortsId);

        try {
            ShortsReplyListResponseDTO replyList = shortsReplyService.retrieve(shortsId);

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

    @PostMapping("/{shortsId}")
    public ResponseEntity<?> addReply(
            @PathVariable Long shortsId,
            @RequestBody ShortsPostRequestDTO dto) {

        log.info("api/shorts/reply/{} : Post!", shortsId);
        log.warn("전달받은 데이터 : {}", dto);

        try {
            if (dto.getContext().isEmpty() || dto.getWriterId().isEmpty())
                throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");

            ShortsReplyListResponseDTO replyList = shortsReplyService.insertShortsReply(shortsId, dto);
            return ResponseEntity.ok().body(replyList);

        } catch (DTONotFoundException e) {
            log.warn("필요한 정보를 전달받지 못했습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{shortsId}/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long shortsId,
                                         @PathVariable Long replyId) {
        log.info("api/shorts/reply/{}/{} : Delete!", shortsId, replyId);

        if(shortsId == null || replyId == null) {
            return ResponseEntity
                    .badRequest()
                    .body(ShortsReplyListResponseDTO
                            .builder()
                            .error("ID값을 보내주세요!")
                            .build());
        }

        try {
            ShortsReplyListResponseDTO replyList = shortsReplyService.deleteShortsReply(shortsId, replyId);

            return ResponseEntity.ok().body(replyList);
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
