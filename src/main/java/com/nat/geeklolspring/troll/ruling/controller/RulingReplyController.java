package com.nat.geeklolspring.troll.ruling.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingReplyPostRequestDTO;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingReplyUpdateRequestDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingReplyListResponseDTO;
import com.nat.geeklolspring.troll.ruling.service.RulingReplyService;
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
@RequestMapping("/troll/ruling/reply")
public class RulingReplyController {
    private final RulingReplyService rulingReplyService;

    // 댓글 정보를 가져오는 컨트롤러
    @GetMapping("/{rulingId}")
    public ResponseEntity<?> replyList(
            @PathVariable Long rulingId,
            // 값이 주어지지 않으면 디폴트로 1페이지와 5개씩 로드하도록 전달
            @PageableDefault(page = 1, size = 5) Pageable pageInfo) {
        log.info("/api/shorts/reply/{} : Get!",rulingId);

        try {
            // 댓글 리스트를 가져오는 부분
            RulingReplyListResponseDTO replyList = rulingReplyService.retrieve(rulingId, pageInfo);

            if(replyList.getReply().isEmpty()) {
                // 댓글 리스트가 비어있으면 실행되는 부분
                // ShortsReplyListResponseDTO 안에 있는 error에 에러로그를 담는다.
                return ResponseEntity
                        .badRequest()
                        .body(RulingReplyListResponseDTO
                                .builder()
                                .error("아직 댓글이 없습니다.")
                                .build());
            }

            // 정상적으로 실행되서 댓글 리스트를 리턴하는 부분
            ResponseEntity<RulingReplyListResponseDTO> body = ResponseEntity.ok().body(replyList);
//            log.error("body replyList : {}",body);
            return body;

        } catch (BadRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(RulingReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(RulingReplyListResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    // 해당 쇼츠에 댓글을 등록하는 컨트롤러
    @PostMapping("/{rulingId}")
    public ResponseEntity<?> addReply(
            @PathVariable Long rulingId,
            @RequestBody RulingReplyPostRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 5) Pageable pageInfo
    ) {

        log.info("api/shorts/reply/{} : Post!", rulingId);
        log.warn("전달받은 데이터 : {}", dto);

        try {
            // 데이터를 정상적으로 전달받았는지 확인
            // 전달받지 못했으면 커스텀 에러인 DTONotFoundException을 만든다.
            if (dto.getContext().isEmpty())
                throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");

            // 댓글을 DB에 저장하는 service 호출, 새 댓글만 리턴받음
            RulingReplyListResponseDTO replyList = rulingReplyService.insertShortsReply(rulingId, dto, userInfo, pageInfo);
            return ResponseEntity.ok().body(replyList);

        } catch (DTONotFoundException e) {
            log.warn("필요한 정보를 전달받지 못했습니다.");
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 댓글Id에 해당하는 댓글을 삭제하는 컨트롤러
    @DeleteMapping("/{replyId}")
    public ResponseEntity<?> deleteReply(@PathVariable Long replyId,
                                         @AuthenticationPrincipal TokenUserInfo userInfo) {
        log.info("api/shorts/reply/{} : Delete!", replyId);

        // 데이터를 정상적으로 전달받았는지 확인
        if(replyId == null) {
            return ResponseEntity
                    .badRequest()
                    .body(RulingReplyListResponseDTO
                            .builder()
                            .error("ID값을 보내주세요!")
                            .build());
        }

        try {
            // replyId에 맞는 댓글을 삭제하는 서비스 실행
            rulingReplyService.deleteShortsReply(replyId, userInfo);

            return ResponseEntity.ok().body(null);
        } catch (NotEqualTokenException e) {
            log.warn("댓글 작성자만 삭제할 수 있습니다!");
            return ResponseEntity
                    .badRequest()
                    .body(RulingReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(RulingReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
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
            RulingReplyListResponseDTO applyBoardList = rulingReplyService.findMyReply(userInfo,pageInfo);
            return ResponseEntity.ok().body(applyBoardList);
        }catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(RulingReplyListResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }
}
