package com.nat.geeklolspring.user.controller;

import com.nat.geeklolspring.user.dto.request.SocialLoginRequestDTO;
import com.nat.geeklolspring.user.dto.response.LoginResponseDTO;
import com.nat.geeklolspring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth/google")
public class GoogleLoginController {

    private final UserService userService;

    @SneakyThrows
    @PostMapping("/social-login")
    public ResponseEntity<LoginResponseDTO> doSocialLogin(
            @RequestBody @Valid SocialLoginRequestDTO request) {

        return ResponseEntity.created(URI.create("/social-login"))
                .body(userService.doSocialLogin(request));
    }



}
