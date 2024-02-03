package com.nat.geeklolspring.troll.ruling.service;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingCheck;
import com.nat.geeklolspring.troll.ruling.dto.response.ProsAndConsDTO;
import com.nat.geeklolspring.troll.ruling.repository.RulingVoteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RulingVoteService {
    private final RulingVoteRepository rvr;

    //찬성에 투표한 내용을 저장
    public boolean rulingVoteSave(RulingCheck rulingCheck){
        if (rulingCheck == null){
            throw new RuntimeException("투표내용이 없습니다.");
        }
        try {
            rvr.save(rulingCheck);
            return true;
        }catch (Exception e){
            log.warn(e.getMessage());
            return false;
        }
    }
    //찬성,반대 총 득표율 계산
    public ProsAndConsDTO prosAndCons(BoardRuling boardRuling){
        int pros = rvr.getPros(boardRuling);
        int cons = rvr.getCons(boardRuling);
        int total = pros+cons;
        float prosPercent = ((float) pros /total) * 100;
        float consPercent = ((float) cons /total) * 100;
        return ProsAndConsDTO.builder()
                .pros(prosPercent)
                .cons(consPercent)
                .build();
    }

}
