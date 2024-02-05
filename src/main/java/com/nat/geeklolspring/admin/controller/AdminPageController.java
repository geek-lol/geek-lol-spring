package com.nat.geeklolspring.admin.controller;

import com.nat.geeklolspring.admin.service.AdminPageService;
import com.nat.geeklolspring.auth.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminPageController {

    private final AdminPageService adminPageService;

//    @PostMapping()
//    public ResponseEntity<?> adminPage(
//            @AuthenticationPrincipal TokenUserInfo userInfo
//    ){
//        log.info("/adminPage POST - {}",userInfo);
//
//        if (!userInfo.getRole().toString().equals("ADMIN")){
//            return ResponseEntity.badRequest().body("권한이 없습니다");
//        }
//
//
//
//        return ResponseEntity.ok().body();
//
//    }




}
