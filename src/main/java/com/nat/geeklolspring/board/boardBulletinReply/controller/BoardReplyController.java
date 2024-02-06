package com.nat.geeklolspring.board.boardBulletinReply.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.boardBulletinReply.dto.request.BoardPostRequestDTO;
import com.nat.geeklolspring.board.boardBulletinReply.dto.request.BoardUpdateRequestDTO;
import com.nat.geeklolspring.board.boardBulletinReply.dto.response.BoardReplyListResponseDTO;
import com.nat.geeklolspring.board.boardBulletinReply.service.BoardReplyService;
import com.nat.geeklolspring.exception.BadRequestException;
import com.nat.geeklolspring.exception.DTONotFoundException;
import com.nat.geeklolspring.exception.NotEqualTokenException;
import com.nat.geeklolspring.shorts.shortsboard.dto.response.ShortsListResponseDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsPostRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.request.ShortsUpdateRequestDTO;
import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyListResponseDTO;
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
//@CrossOrigin(origins = {"http://localhost:3000","",""})
@RequestMapping("/board/bulletin/detail/reply")
public class BoardReplyController {
    private final BoardReplyService boardReplyService;

    // 해당 개시글의 댓글 정보를 가져오는 컨트롤러
    @GetMapping("/{bulletinId}")
    public ResponseEntity<?> replyList(
            @PathVariable Long bulletinId,
            // 값이 주어지지 않으면 디폴트로 1페이지와 5개씩 로드하도록 전달
            @PageableDefault(page = 1, size = 5) Pageable pageInfo) {
        log.info("/board/bulletin/detail/{} : Get!", bulletinId);

        try {
            // 댓글 리스트를 가져오는 부분
            BoardReplyListResponseDTO replyList = boardReplyService.retrieve(bulletinId,pageInfo);

            if(replyList.getReply().isEmpty()) {
                // 댓글 리스트가 비어있으면 실행되는 부분
                // ShortsReplyListResponseDTO 안에 있는 error에 에러로그를 담는다.
                return ResponseEntity
                        .badRequest()
                        .body(BoardReplyListResponseDTO
                                .builder()
                                .error("아직 댓글이 없습니다.")
                                .build());
            }

            // 정상적으로 실행되서 댓글 리스트를 리턴하는 부분
            return ResponseEntity.ok().body(replyList);

        } catch (BadRequestException e) {
            return ResponseEntity
                    .badRequest()
                    .body(BoardReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .internalServerError()
                    .body(BoardReplyListResponseDTO
                            .builder()
                            .error(e.getMessage()));
        }
    }

    // 해당 쇼츠에 댓글을 등록하는 컨트롤러
    @PostMapping("/{bulletinId}")
    public ResponseEntity<?> addReply(
            @PathVariable Long bulletinId,
            @RequestBody BoardPostRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 5) Pageable pageInfo
    ) {

        log.info("/board/bulletin/detail/reply/{} : Post!", bulletinId);
        log.warn("전달받은 데이터 : {}", dto);

        try {
            // 데이터를 정상적으로 전달받았는지 확인
            // 전달받지 못했으면 커스텀 에러인 DTONotFoundException을 만든다.
            if (dto.getContext().isEmpty())
                throw new DTONotFoundException("필요한 정보가 입력되지 않았습니다.");

            // 댓글을 DB에 저장하는 service 호출, 새 댓글만 리턴받음
            BoardReplyListResponseDTO replyList = boardReplyService.insertBoardReply(bulletinId, dto, userInfo, pageInfo);
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
        log.info("/board/bulletin/detail/reply{} : Delete!", replyId);

        // 데이터를 정상적으로 전달받았는지 확인
        if(replyId == null) {
            return ResponseEntity
                    .badRequest()
                    .body(BoardReplyListResponseDTO
                            .builder()
                            .error("ID값을 보내주세요!")
                            .build());
        }

        try {
            // replyId에 맞는 댓글을 삭제하는 서비스 실행
            boardReplyService.deleteBoardReply(replyId, userInfo);

            return ResponseEntity.ok().body(null);
        } catch (NotEqualTokenException e) {
            log.warn("댓글 작성자만 삭제할 수 있습니다!");
            return ResponseEntity
                    .badRequest()
                    .body(BoardReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(BoardReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        }
    }


    // 댓글 수정 컨트롤러
    @RequestMapping(method = {PUT, PATCH})
    public ResponseEntity<?> updateBoardReply(@RequestBody BoardUpdateRequestDTO dto,
                                               @AuthenticationPrincipal TokenUserInfo userInfo) {
        log.info("/board/bulletin/detail/reply : PATCH");
        log.debug("서버에서 받은 값 : {}", dto);

        // 데이터를 정상적으로 전달받았는지 확인
        if(dto.getReplyId() == null || dto.getContext().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(BoardReplyListResponseDTO
                            .builder()
                            .error("필요한 값 중에 비어있는 값이 있습니다!")
                            .build());
        }

        try {
            // 댓글을 DB에서 수정하는 서비스 호출
            boardReplyService.updateReply(dto, userInfo);

            return ResponseEntity.ok().body(null);

        } catch (NotEqualTokenException e) {
            log.warn("댓글 작성자만 수정할 수 있습니다!");
            return ResponseEntity
                    .badRequest()
                    .body(BoardReplyListResponseDTO
                            .builder()
                            .error(e.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(BoardReplyListResponseDTO.builder().error(e.getMessage()));
        }
    }
}
