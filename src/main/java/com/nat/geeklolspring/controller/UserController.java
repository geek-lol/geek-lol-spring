package com.nat.geeklolspring.controller;

import com.nat.geeklolspring.dto.request.UserSignUpRequestDTO;
import com.nat.geeklolspring.dto.response.UserSignUpResponseDTO;
import com.nat.geeklolspring.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/sign_up")
public class UserController {

    private final UserService userService;

    @PostMapping
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


}
