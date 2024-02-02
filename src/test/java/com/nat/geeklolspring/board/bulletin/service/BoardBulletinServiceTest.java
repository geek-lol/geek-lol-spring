package com.nat.geeklolspring.board.bulletin.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.board.bulletin.dto.request.BoardBulletinWriteRequestDTO;
import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)
class BoardBulletinServiceTest {

    @Autowired
    BoardBulletinService boardBulletinService;

    @Test
    @DisplayName("게시물 저장 테스트")
    void saveBoard() {
        //given
        BoardBulletinWriteRequestDTO dto =
                BoardBulletinWriteRequestDTO.builder()
                        .title("1")
                        .boardMedia(null)
                        .content("1111")
                        .build();

        String userId = "나나";
        //when
//        BoardBulletinDetailResponseDTO responseDTO = boardBulletinService.create(dto,userId);
        //then

        System.out.println("\n\n\n");
//        System.out.println("responseDTO = " + responseDTO);
        System.out.println("\n\n\n");
    }

    @Test
    @DisplayName("게시판 목록 보여주기")
    void viewBoard() {
        //given

        //when
        boardBulletinService.retrieve();
        //then

    }

    @Test
    @DisplayName("삭제 테스트")
    void deleteTest() {
        //given

        //when

        //then
    }

}