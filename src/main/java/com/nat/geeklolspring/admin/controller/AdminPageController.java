package com.nat.geeklolspring.admin.controller;

import com.nat.geeklolspring.admin.dto.Response.List.*;
import com.nat.geeklolspring.admin.service.AdminPageService;
import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDeleteResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinResponseDTO;
import com.nat.geeklolspring.board.bulletin.service.BoardBulletinService;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.report.dto.response.ReportListResponseDTO;
import com.nat.geeklolspring.report.service.ReportService;
import com.nat.geeklolspring.shorts.shortsboard.service.ShortsService;
import com.nat.geeklolspring.troll.apply.dto.request.ApplyDeleteRequestDTO;
import com.nat.geeklolspring.troll.apply.service.RulingApplyService;
import com.nat.geeklolspring.troll.ruling.service.RulingBoardService;
import com.nat.geeklolspring.user.dto.request.UserDeleteRequestDTO;
import com.nat.geeklolspring.user.service.UserService;
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
@RequestMapping("/admin")
public class AdminPageController {

    private final AdminPageService adminPageService;
    private final ReportService reportService;
    private final UserService userService;
    private final BoardBulletinService boardBulletinService;
    private final ShortsService shortsService;
    private final RulingApplyService rulingApplyService;
    private final RulingBoardService rulingBoardService;
    @PostMapping("/user")
    public ResponseEntity<?> adminPageUserContent(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("/adminPage/user POST");

        if (!userInfo.getRole().toString().equals("ADMIN")){
            return ResponseEntity.badRequest().body("권한이 없습니다");
        }

        AdminPageUserListResponseDTO userList = adminPageService.userView(pageInfo);

        return ResponseEntity.ok().body(userList);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> userDelete(
            @Validated @RequestBody UserDeleteRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        try {

            if (!userInfo.getRole().toString().equals("ADMIN")){
                return ResponseEntity.badRequest().body("권한이 없습니다");
            }

            userService.delete(dto);
            log.info("delete user : {}",dto.getId());
            AdminPageUserListResponseDTO userList = adminPageService.userView(pageInfo);
            return ResponseEntity.ok().body(userList);
        }catch (RuntimeException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body("삭제에 실패했습니다");
        }
    }

    @PostMapping("/board")
    public ResponseEntity<?> adminPageBoardContent(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("/adminPage/board POST ");

        if (!userInfo.getRole().toString().equals("ADMIN")){
            return ResponseEntity.badRequest().body("권한이 없습니다");
        }

        AdminPageBoardBulletinListResponseDTO boardList = adminPageService.boardView(pageInfo);

        return ResponseEntity.ok().body(boardList);
    }

    @DeleteMapping("/board")
    public ResponseEntity<?> boardDelete(
            @Validated @RequestBody BoardBulletinDeleteResponseDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ) {
        try {
            if (!userInfo.getRole().toString().equals("ADMIN")){
                return ResponseEntity.badRequest().body("권한이 없습니다");
            }
            boardBulletinService.delete(dto);
            log.info("delete board : {}",dto.getTitle());
            AdminPageBoardBulletinListResponseDTO boardList = adminPageService.boardView(pageInfo);
            return ResponseEntity.ok().body(boardList);
        }catch (RuntimeException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body("삭제에 실패했습니다");
        }
    }

    @PostMapping("/shorts")
    public ResponseEntity<?> adminPageShortsContent(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("/adminPage/board POST ");

        if (!userInfo.getRole().toString().equals("ADMIN")){
            return ResponseEntity.badRequest().body("권한이 없습니다");
        }

        AdminPageShortsListResponseDTO shortsList = adminPageService.shortsView(pageInfo);

        return ResponseEntity.ok().body(shortsList);
    }

    @DeleteMapping("/shorts")
    public ResponseEntity<?> shortsDetail(
            @PathVariable Long shortsId,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ) {
        try {
            if (!userInfo.getRole().toString().equals("ADMIN")){
                return ResponseEntity.badRequest().body("권한이 없습니다");
            }
            shortsService.deleteShorts(shortsId);
            AdminPageShortsListResponseDTO shortsList = adminPageService.shortsView(pageInfo);
            return ResponseEntity.ok().body(shortsList);
        }catch (RuntimeException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body("삭제에 실패했습니다");
        }
    }


    @PostMapping("/report")
    public ResponseEntity<?> adminPageReportContent(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("/adminPage/board POST ");

        if (!userInfo.getRole().toString().equals("ADMIN")){
            return ResponseEntity.badRequest().body("권한이 없습니다");
        }

        ReportListResponseDTO reportList = reportService.retrieveView(pageInfo);

        return ResponseEntity.ok().body(reportList);
    }

    @PostMapping("/applys")
    public ResponseEntity<?> adminPageApplyContent(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("/adminPage/applys POST ");

        if (!userInfo.getRole().toString().equals("ADMIN")){
            return ResponseEntity.badRequest().body("권한이 없습니다");
        }

        AdminPageRulingApplyListResponseDTO applyList = adminPageService.applyView(pageInfo);

        return ResponseEntity.ok().body(applyList);
    }

    @DeleteMapping("/applys")
    public ResponseEntity<?> applysDetail(
            @PathVariable ApplyDeleteRequestDTO dto,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ) {
        try {
            if (!userInfo.getRole().toString().equals("ADMIN")){
                return ResponseEntity.badRequest().body("권한이 없습니다");
            }
            rulingApplyService.deleteBoard(userInfo,dto);
            AdminPageRulingApplyListResponseDTO applyList = adminPageService.applyView(pageInfo);
            return ResponseEntity.ok().body(applyList);
        }catch (RuntimeException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body("삭제에 실패했습니다");
        }
    }

    @PostMapping("/ruling")
    public ResponseEntity<?> adminPageRulingContent(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ){
        log.info("/adminPage/applys POST ");

        if (!userInfo.getRole().toString().equals("ADMIN")){
            return ResponseEntity.badRequest().body("권한이 없습니다");
        }

        AdminPageRulingListResponseDTO rulingList = adminPageService.rulingView(pageInfo);

        return ResponseEntity.ok().body(rulingList);
    }

    @DeleteMapping("/ruling/{rulingId}")
    public ResponseEntity<?> rulingDetail(
            @PathVariable Long rulingId,
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @PageableDefault(page = 1, size = 10) Pageable pageInfo
    ) {
        try {
            if (!userInfo.getRole().toString().equals("ADMIN")){
                return ResponseEntity.badRequest().body("권한이 없습니다");
            }
            rulingBoardService.deleteBoard(userInfo,rulingId);
            AdminPageRulingListResponseDTO applyList = adminPageService.rulingView(pageInfo);
            return ResponseEntity.ok().body(applyList);
        }catch (RuntimeException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body("삭제에 실패했습니다");
        }
    }



}
