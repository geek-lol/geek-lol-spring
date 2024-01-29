package com.nat.geeklolspring.user.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.user.dto.request.LoginRequestDTO;
import com.nat.geeklolspring.user.dto.request.UserSignUpRequestDTO;
import com.nat.geeklolspring.user.dto.response.LoginResponseDTO;
import com.nat.geeklolspring.user.dto.response.UserSignUpResponseDTO;
import com.nat.geeklolspring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/sign_up")
    public ResponseEntity<?> signUp(
            @Validated @RequestBody UserSignUpRequestDTO dto,
            BindingResult result
            ){
        log.info("/api/auth/ POST - {}",dto);

        if (result.hasErrors()){
            log.warn(result.toString());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }

        try {
            UserSignUpResponseDTO responseDTO = userService.create(dto);
            return ResponseEntity.ok().body(responseDTO);
        }catch (Exception e){
            log.warn("문제 발생");
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/check")
    public ResponseEntity<?> check(String id){
        boolean flag = userService.isDupilcateId(id);
        log.debug("{} 중복 - {}",id,flag);

        return ResponseEntity.badRequest().body(flag);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(
            @Validated @RequestBody LoginRequestDTO dto
            )
        {try {
            LoginResponseDTO responseDTO = userService.authenticate(dto);
            log.info("login success by {}",responseDTO.getUserName());
            return ResponseEntity.ok().body(responseDTO);
        }catch (RuntimeException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/modify")
    public ResponseEntity<?> modify(
            TokenUserInfo userInfo,
            @Validated @RequestPart("user")UserSignUpRequestDTO dto,
            @RequestPart("profileImage") MultipartFile profileImg,
            BindingResult result
    ) {

        if (result.hasErrors()) {
            log.warn(result.toString());
            return ResponseEntity
                    .badRequest()
                    .body(result.getFieldError());
        }

        try {

            String uploadProfileImagePath = null;
            if (profileImg != null) {
                log.info("file-name: {}", profileImg.getOriginalFilename());
                uploadProfileImagePath = userService.uploadProfileImage(profileImg);
            }
            LoginResponseDTO responseDTO = userService.modify(userInfo,dto, uploadProfileImagePath);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception e) {
            log.warn("문제 발생");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
