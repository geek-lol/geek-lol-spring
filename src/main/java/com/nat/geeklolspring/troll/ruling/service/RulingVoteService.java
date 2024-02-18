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
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RulingVoteService {
    private final RulingVoteRepository rvr;
    private final BoardRulingRepository boardRulingRepository;
    private final UserRepository userRepository;

    @Transactional()
    // 투표한 내용을 저장
    public ProsAndConsDTO rulingVoteSave(RulingVoteRequestDTO dto,TokenUserInfo userInfo){
        //현재 접속한 게시물
        BoardRuling boardRuling = boardRulingRepository.findById(dto.getRulingId()).orElseThrow();
        // 로그인 한 유저
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();

        //현재 투표중인 보드
        BoardRuling board = boardRulingRepository.findTopByOrderByRulingDateDesc();

        log.info("현재 투표중인 보드 : {} ",board);

        if (!boardRuling.equals(board)){ //보드가 최신이 아니면 ㄱㄱ
            ProsAndConsDTO prosAndConsDTO = prosAndCons(boardRuling.getRulingId());
            prosAndConsDTO.setError("지난 투표게시물입니다.");
            return prosAndConsDTO;
        } else if (rvr.existsByRulingVoterAndRulingId(user,board)){ //최신 보드에 투표 이력이 있으면 ㄱㄱ
            ProsAndConsDTO prosAndConsDTO = prosAndCons(boardRuling.getRulingId());
            prosAndConsDTO.setError("이미 투표한 회원입니다.");
            return prosAndConsDTO;
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
