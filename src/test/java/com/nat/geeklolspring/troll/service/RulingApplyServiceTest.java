package com.nat.geeklolspring.troll.service;

import com.nat.geeklolspring.troll.apply.dto.response.RulingApplyResponseDTO;
import com.nat.geeklolspring.troll.apply.service.RulingApplyService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RulingApplyServiceTest {

    @Autowired
    RulingApplyService ras;
    @Test
    @DisplayName("전체 조회 하면 1개의 length를 가진다")
    void findAllBoard(){
        //given

        //when
        RulingApplyResponseDTO allBoard = ras.findAllBoard();
        //then
        assertEquals(1,allBoard.getBoardApply().size());
    }

//    @Test
//    @DisplayName("게시판 등록에 성공하면 전체 게시물은 2 length를 가진다.")
//    void createBoard() {
//        //given
//        RulingApplyRequestDTO build = RulingApplyRequestDTO.builder()
//                .title("제목2")
//                .content("내용2")
//                .build();
//        TokenUserInfo seon = TokenUserInfo.builder()
//                .userId("seon")
//                .build();
//        //when
//        RulingApplyResponseDTO board = ras.createBoard(build, seon);
//
//        //then
//        assertEquals(2,board.getBoardApply().size());
//    }
}