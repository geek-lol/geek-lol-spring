package com.nat.geeklolspring.troll.service;

import com.nat.geeklolspring.troll.apply.repository.BoardRulingRepository;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.troll.apply.dto.response.ProsAndConsDTO;
import com.nat.geeklolspring.troll.apply.service.RulingVoteService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.transaction.Transactional;

@SpringBootTest
@Transactional
@Rollback
class RulingVoteServiceTest {
    @Autowired
    RulingVoteService rulingVoteService;

    @Autowired
    BoardRulingRepository br;

    @Test
    @DisplayName("찬성과 반대의 퍼센트가 나와야한다.")
    void percentTest() {
        //given
        BoardRuling boardRuling = br.findById(2L).orElseThrow();
        //when
        ProsAndConsDTO prosAndConsDTO = rulingVoteService.prosAndCons(boardRuling);
        //then
        System.out.println("\n\n\n");
        System.out.println("prosAndConsDTO = " + prosAndConsDTO);
        System.out.println("\n\n\n");
    }

}