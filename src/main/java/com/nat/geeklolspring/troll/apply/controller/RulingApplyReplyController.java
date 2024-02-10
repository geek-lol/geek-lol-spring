package com.nat.geeklolspring.troll.apply.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsUpdateRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyListResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyDeleteRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyReplyPostRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyReplyUpdateRequestDTO;
import com.nat.geeklolspring.troll.apply.dto.response.ApplyReplyListResponseDTO;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.apply.service.ApplyReplyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.PATCH;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@Slf4j
@RequiredArgsConstructor

@RequestMapping("/troll/apply/reply")
public class RulingApplyReplyController {
    private final ApplyReplyService applyReplyService;

    // 해당 쇼츠의 댓글 정보를 가져오는 컨트롤러
    @GetMapping("/{applyId}")
    public ResponseEntity<?> replyList(
            @PathVariable Long applyId,
            // 값이 주어지지 않으면 디폴트로 1페이지와 5개씩 로드하도록 전달
            @PageableDefault(page = 1, size = 5) Pageable pageInfo) {
        log.info("/api/shorts/reply/{} : Get!",applyId);

        try {
            // 댓글 리스트를 가져오는 부분
            ApplyReplyListResponseDTO replyList = applyReplyService.retrieve(applyId, pageInfo);

            if(replyList.getReply().isEmpty()) {
                // 댓글 리스트가 비어있으면 실행되는 부분
                // ShortsReplyListResponseDTO 안에 있는 error에 에러로그를 담는다.
                return ResponseEntity
                        .badRequest()
                        .body(ApplyReplyListResponseDTO
                                .builder()
                                .error("아직 댓글이 없습니다.")
                                .build());
            }

            // 정상적으로 실행되서 댓글 리스트를 리턴하는 부분
            return ResponseEntity.ok().body(replyList);

        } catch (BadRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApplyReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(ApplyReplyListResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    // 댓글을 등록하는 컨트롤러
    @PostMapping("/{applyId}")
    public ResponseEntity<?> addReply(
            @PathVariable Long applyId,
            @RequestBody ApplyReplyPostRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 5) Pageable pageInfo
    ) {

        log.info("api/shorts/reply/{} : Post!", applyId);
        log.warn("전달받은 데이터 : {}", dto);

        try {
            // 데이터를 정상적으로 전달받았는지 확인
            // 전달받지 못했으면 커스텀 에러인 DTONotFoundException을 만든다.
            if (dto.getContext().isEmpty())
                throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");

            // 댓글을 DB에 저장하는 service 호출, 새 댓글만 리턴받음
            ApplyReplyListResponseDTO replyList = applyReplyService.insertReply(applyId, dto, userInfo, pageInfo);
            return ResponseEntity.ok().body(null);

        } catch (DTONotFoundException e) {
            log.warn("필요한 정보를 전달받지 못했습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 댓글Id에 해당하는 댓글을 삭제하는 컨트롤러
    @DeleteMapping("")
    public ResponseEntity<?> deleteReply(
            @RequestBody ApplyDeleteRequestDTO replyId,
            @AuthenticationPrincipal TokenUserInfo userInfo) {
        log.info("api/shorts/reply/{} : Delete!", replyId);

        // 데이터를 정상적으로 전달받았는지 확인
        if(replyId == null) {
            return ResponseEntity
                    .badRequest()
                    .body(ShortsReplyListResponseDTO
                            .builder()
                            .error("ID값을 보내주세요!")
                            .build());
        }

        try {
            // replyId에 맞는 댓글을 삭제하는 서비스 실행
            applyReplyService.deleteShortsReply(replyId, userInfo);

            return ResponseEntity.ok().body(null);
        } catch (NotEqualTokenException e) {
            log.warn("댓글 작성자만 삭제할 수 있습니다!");
            return ResponseEntity
                    .badRequest()
                    .body(ApplyReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(ApplyReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        }
    }


    // 댓글 수정 컨트롤러
    @RequestMapping(method = {PUT, PATCH})
    public ResponseEntity<?> updateShortsReply(@RequestBody ApplyReplyUpdateRequestDTO dto,
                                               @AuthenticationPrincipal TokenUserInfo userInfo) {
        log.info("api/shorts/reply : PATCH");
        log.debug("서버에서 받은 값 : {}", dto);

        // 데이터를 정상적으로 전달받았는지 확인
        if(dto.getReplyId() == null || dto.getContext().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(ApplyReplyListResponseDTO
                            .builder()
                            .error("필요한 값 중에 비어있는 값이 있습니다!")
                            .build());
        }

        try {
            // 댓글을 DB에서 수정하는 서비스 호출
            applyReplyService.updateReply(dto, userInfo);

            return ResponseEntity.ok().body(null);

        } catch (NotEqualTokenException e) {
            log.warn("댓글 작성자만 수정할 수 있습니다!");
            return ResponseEntity
                    .badRequest()
                    .body(ApplyReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApplyReplyListResponseDTO.builder().error(e.getMessage()));
        }
    }

    //로그인한 사람 댓글 조회하기
    @GetMapping("/my")
    public ResponseEntity<?> findMyBoard(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("나의 페이지 트롤 지원 댓글 조회 실행");
        try {
            ApplyReplyListResponseDTO applyApplyList = applyReplyService.findMyReply(userInfo,pageInfo);
            return ResponseEntity.ok().body(applyApplyList);
        }catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(ApplyReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        }
    }
}
