package com.nat.geeklolspring.troll.service;

import com.nat.geeklolspring.board.ruling.repository.BoardRulingRepository;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingReply;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@Transactional
//@Rollback
class RulingReplyServiceTest {

    @Autowired
    RulingReplyService rulingReplyService;

    @Autowired
    UserRepository userRepository;
    @Autowired
    BoardRulingRepository boardRulingRepository;

    @Test
    @DisplayName("1번 게시글에 청하의 댓글이 등록되어야한다.")
    void createReply () {
        //given
        User seon = userRepository.findById("seon").orElseThrow();
        BoardRuling br = boardRulingRepository.findById(1L).orElseThrow();
        RulingReply stq = RulingReply.builder()
                .context("stq")
                .rulingWriterId(seon)
                .rulingId(br)
                .build();
        //when
        boolean b = rulingReplyService.rulingReplySave(stq);
        //then
        assertTrue(b);
    }

    @Test
    @DisplayName("모든 댓글을 조회하면 1개가 나온다")
    void findAllTest() {
        //given
        BoardRuling br = boardRulingRepository.findById(2L).orElseThrow();
        //when
        List<RulingReply> rulingReplies = rulingReplyService.rulingReplyAll(br);
        //then
        assertEquals(1,rulingReplies.size());
    }
    @Test
    @DisplayName("아이디가 1인 댓글을 삭제하면 빈 리스트를 반환한다.")
    void deleteTest() {
        //given
        Long id = 1L;
        //when
        List<RulingReply> rulingReplies = rulingReplyService.rulingReplyDelete(id);
        //then
        assertEquals(0,rulingReplies.size());
    }
}