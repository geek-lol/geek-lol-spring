package com.nat.geeklolspring.troll.ruling.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingCheck;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.troll.ruling.dto.request.RulingVoteRequestDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.ProsAndConsDTO;
import com.nat.geeklolspring.troll.ruling.repository.BoardRulingRepository;
import com.nat.geeklolspring.troll.ruling.repository.RulingVoteRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RulingVoteService {
    private final RulingVoteRepository rvr;
    private final BoardRulingRepository boardRulingRepository;
    private final UserRepository userRepository;

    // 투표한 내용을 저장
    public ProsAndConsDTO rulingVoteSave(RulingVoteRequestDTO dto,TokenUserInfo userInfo){
        BoardRuling boardRuling = boardRulingRepository.findById(dto.getRulingId()).orElseThrow();
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();

        //현재 투표중인 보드
        BoardRuling board = boardRulingRepository.findTopByOrderByRulingDateDesc();

        //저장된 정보가 있는지 여부 - 있으면 true / 없으면 false
        boolean flag = rvr.existsByRulingVoter(user);
        // 보드가 같으면 true / 다르면 false
        boolean boardFlag = board.equals(boardRuling);

         if (flag || !boardFlag) {
            return null;
        }

        if (dto.getVote().equals("pros")){
            RulingCheck check = RulingCheck.builder()
                    .rulingVoter(user)
                    .pros(1)
                    .cons(0)
                    .rulingId(boardRuling)
                    .build();
            rvr.save(check);
        }
        if (dto.getVote().equals("cons")){
            RulingCheck check = RulingCheck.builder()
                    .rulingVoter(user)
                    .pros(0)
                    .cons(1)
                    .rulingId(boardRuling)
                    .build();
            rvr.save(check);
        }
        return prosAndCons(dto.getRulingId());
    }
    //찬성,반대 총 득표율 계산
    public ProsAndConsDTO prosAndCons(Long rulingId){
        BoardRuling boardRuling = boardRulingRepository.findById(rulingId).orElseThrow();
        int pros = rvr.countByRulingIdAndPros(boardRuling,1);
        int cons = rvr.countByRulingIdAndCons(boardRuling,1);

        int total = pros+cons;
        float prosPercent = ((float) pros /total) * 100;
        float consPercent = ((float) cons /total) * 100;
        return ProsAndConsDTO.builder()
                .pros(pros)
                .cons(cons)
                .prosPercent(prosPercent)
                .consPercent(consPercent)
                .build();
    }


}
