package com.nat.geeklolspring.troll.repository;

import com.nat.geeklolspring.board.ruling.repository.BoardRulingRepository;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingCheck;
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
class RulingVoteRepositoryTest {

    @Autowired
    RulingVoteRepository rulingVoteRepository;
    @Autowired
    BoardRulingRepository brr;
    @Autowired
    UserRepository ur;
    @Test
    @DisplayName("저장한다")
    void saveUs() {
        User user = ur.findById("seon").orElseThrow();
        BoardRuling boardRuling = brr.findById(2L).orElseThrow();
        RulingCheck b1 = RulingCheck.builder()
                .rulingVoteId(1L)
                .rulingId(boardRuling)
                .one(1)
                .two(0)
                .rulingVoter(user)
                .build();
        RulingCheck b2 = RulingCheck.builder()
                .rulingVoteId(2L)
                .rulingId(boardRuling)
                .one(0)
                .two(1)
                .rulingVoter(user)
                .build();
        RulingCheck b3 = RulingCheck.builder()
                .rulingVoteId(3L)
                .rulingId(boardRuling)
                .one(1)
                .two(0)
                .rulingVoter(user)
                .build();
        RulingCheck b4 = RulingCheck.builder()
                .rulingVoteId(4L)
                .rulingId(boardRuling)
                .one(1)
                .two(0)
                .rulingVoter(user)
                .build();

        rulingVoteRepository.save(b1);
        rulingVoteRepository.save(b2);
        rulingVoteRepository.save(b3);
        rulingVoteRepository.save(b4);

        List<RulingCheck> all = rulingVoteRepository.findAll();
        assertEquals(4,all.size());

    }

    @Test
    @DisplayName("1번 게시물의 총 찬성표는 3개 이다.")
    void prosTest() {
        //given
        Long boardId = 2L;
        BoardRuling boardRuling = brr.findById(boardId).orElseThrow();
        //when
        int pros = rulingVoteRepository.getPros(boardRuling);
        //then
        assertEquals(3,pros);
    }
    @Test
    @DisplayName("1번 게시물의 총 찬성표는 3개 이다.")
    void consTest() {
        //given
        Long boardId = 2L;
        BoardRuling boardRuling = brr.findById(boardId).orElseThrow();
        //when
        int cons = rulingVoteRepository.getCons(boardRuling);
        //then
        assertEquals(1,cons);
    }
}