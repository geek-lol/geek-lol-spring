package com.nat.geeklolspring.service;

import com.nat.geeklolspring.user.dto.request.UserSignUpRequestDTO;
import com.nat.geeklolspring.user.dto.response.UserSignUpResponseDTO;
import com.nat.geeklolspring.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(false)
class UserServiceTest {


    @Autowired
    UserService userService;

    @Test
    @DisplayName("회원가입을 하면 비밀번호가 인코딩되어 디비에 저장된다")
    void saveTest() {
        //given
        UserSignUpRequestDTO dto = UserSignUpRequestDTO.builder()
                .id("abc123")
                .password("abc12356789")
                .userName("나")
                .profileIamge(null)
                .build();
        //when
        UserSignUpResponseDTO responseDTO = userService.create(dto);

        //then
        assertEquals("나", responseDTO.getUserName());

        System.out.println("\n\n\n");
        System.out.println("responseDTO = " + responseDTO);
        System.out.println("\n\n\n");
    }



}