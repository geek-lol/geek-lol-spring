package com.nat.geeklolspring.user.controller;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.user.dto.request.LoginRequestDTO;
import com.nat.geeklolspring.user.dto.request.UserDeleteRequestDTO;
import com.nat.geeklolspring.user.dto.request.UserModifyRequestDTO;
import com.nat.geeklolspring.user.dto.request.UserSignUpRequestDTO;
import com.nat.geeklolspring.user.dto.response.LoginResponseDTO;
import com.nat.geeklolspring.user.dto.response.UserResponseDTO;
import com.nat.geeklolspring.user.dto.response.UserSignUpResponseDTO;
import com.nat.geeklolspring.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //회원 조회 하기
    @GetMapping
    public ResponseEntity<?> findMyInfo(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        try {

            UserResponseDTO byUserInfo = userService.findByUserInfo(userInfo);
            return ResponseEntity.ok().body(byUserInfo);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
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
    @GetMapping("/pwcheck")
    public ResponseEntity<?> pwCheck(
            String pw
            , @AuthenticationPrincipal TokenUserInfo userInfo
    ){
        boolean flag = userService.isDupilcatePw(pw, userInfo);
        log.info("{} 일치 - {}",pw,flag);
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


    @PostMapping("/delete")
    public void delete(
            @Validated @RequestBody UserDeleteRequestDTO dto
            )
    {
        try {
            userService.delete(dto);
            log.info("delete user : {}",dto.getId());
        }catch (RuntimeException e){
            log.warn(e.getMessage());
        }
    }

    @PutMapping("/modify")
    public ResponseEntity<?> modify(
            @AuthenticationPrincipal TokenUserInfo userInfo,
            @Validated @RequestPart(value = "user") UserModifyRequestDTO dto,
            @RequestPart(value = "profileImage",required = false) MultipartFile profileImg,
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
        } catch (IllegalStateException e){
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (Exception e){
            log.warn(e.getMessage());
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    //이미지 파일 불러오기
    @GetMapping("/load-profile")
    public ResponseEntity<?> loadProfile(
            @AuthenticationPrincipal TokenUserInfo userInfo
    ){

        try {
            String profilePath = userService.getProfilePath(userInfo.getUserId());

            File profileFile = new File(profilePath);

            if (!profileFile.exists()) return ResponseEntity.notFound().build();

            byte[] fileData = FileCopyUtils.copyToByteArray(profileFile);

            HttpHeaders headers = new HttpHeaders();


            MediaType mediaType = extractFileExtension(profilePath);
            if (mediaType == null){
                return ResponseEntity.internalServerError().body("이미지가 아닙니다");
            }

            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
    //특정 유저의 이미지 파일 불러오기
    @GetMapping("/profile")
    public ResponseEntity<?> loadProfileByUserId(
            @RequestParam String userId
    ){
        try {
            String profilePath = userService.getProfilePath(userId);

            File profileFile = new File(profilePath);

            if (!profileFile.exists()) return ResponseEntity.notFound().build();

            byte[] fileData = FileCopyUtils.copyToByteArray(profileFile);

            HttpHeaders headers = new HttpHeaders();


            MediaType mediaType = extractFileExtension(profilePath);
            if (mediaType == null){
                return ResponseEntity.internalServerError().body("이미지가 아닙니다");
            }

            headers.setContentType(mediaType);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(fileData);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(e.getMessage());
        }

    }
    private MediaType extractFileExtension(String filePath){

        String ext = filePath.substring(filePath.lastIndexOf(".") + 1);

        switch (ext.toUpperCase()) {
            case "JPEG": case "JPG":
                return MediaType.IMAGE_JPEG;
            case "PNG":
                return MediaType.IMAGE_PNG;
            case "GIF":
                return MediaType.IMAGE_GIF;
            default:
                return null;
        }

    }

}
