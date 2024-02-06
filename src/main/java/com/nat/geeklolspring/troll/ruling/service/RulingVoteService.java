package com.nat.geeklolspring.troll.ruling.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.RulingCheck;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingVoteRequestDTO;
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

    // 투표한 내용을 저장
    public ProsAndConsDTO rulingVoteSave(RulingVoteRequestDTO dto,TokenUserInfo userInfo){
        if (dto.getVote().equals("pros")){
            RulingCheck check = RulingCheck.builder()
                    .rulingVoter(userInfo.getUserId())
                    .pros(1)
                    .cons(0)
                    .rulingId(dto.getRulingId())
                    .build();
            rvr.save(check);
        }
        if (dto.getVote().equals("cons")){
            RulingCheck check = RulingCheck.builder()
                    .rulingVoter(userInfo.getUserId())
                    .pros(0)
                    .cons(1)
                    .rulingId(dto.getRulingId())
                    .build();
            rvr.save(check);
        }
        return prosAndCons(dto.getRulingId());
    }
    //찬성,반대 총 득표율 계산
    public ProsAndConsDTO prosAndCons(Long rulingId){
        int pros = rvr.countByRulingIdAndPros(rulingId,1);
        int cons = rvr.countByRulingIdAndCons(rulingId,1);

        int total = pros+cons;
        float prosPercent = ((float) pros /total) * 100;
        float consPercent = ((float) cons /total) * 100;
        return ProsAndConsDTO.builder()
                .pros(prosPercent)
                .cons(consPercent)
                .build();
    }

}
