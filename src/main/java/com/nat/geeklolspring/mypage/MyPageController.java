package com.nat.geeklolspring.mypage;

import com.nat.geeklolspring.auth.TokenUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/count")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping("")
    public ResponseEntity<?> countMyBoard(
            @AuthenticationPrincipal TokenUserInfo userInfo
            ){
        try {
            MyPageResponseDTO dto = myPageService.showCount(userInfo);
            return ResponseEntity.ok().body(dto);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}