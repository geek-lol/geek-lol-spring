package com.nat.geeklolspring.game.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.CsGameRank;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.game.dto.request.GameRankRequestDTO;
import com.nat.geeklolspring.game.dto.response.GameRankListResponseDTO;
import com.nat.geeklolspring.game.dto.response.GameRankResponseDTO;
import com.nat.geeklolspring.game.repository.CsGameRankRepository;
import com.nat.geeklolspring.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class CsGameRankService {
    private final CsGameRankRepository csGameRankRepository;
    private final UserRepository userRepository;

    // 랭킹 조회
    public GameRankListResponseDTO findRank(){
        List<CsGameRank> rankList = csGameRankRepository.findTop10ByOrderByScoreDesc();
        List<GameRankResponseDTO> dtoList = rankList.stream()
                .map(GameRankResponseDTO::new)
                .collect(Collectors.toList());

        return GameRankListResponseDTO.builder()
                .gameRankList(dtoList)
                .build();
    }
    // 랭킹 저장
    @Transactional
    public GameRankListResponseDTO addRank(GameRankRequestDTO dto, TokenUserInfo userInfo){
        User user = userRepository.findById(userInfo.getUserId()).orElseThrow();
        // 근데 랭킹에 이미 있고, 원래 점수보다 높으면 수정해서 저장
        CsGameRank userRankInfo = csGameRankRepository.findByUserId(userInfo.getUserId());
        GameRankResponseDTO gameRankResponseDTO;
        if (userRankInfo != null){ //랭킹에 저장된 정보가 있음
            gameRankResponseDTO = new GameRankResponseDTO(userRankInfo);
            if (gameRankResponseDTO.getScore() < dto.getScore()){
                gameRankResponseDTO.setScore(dto.getScore());
            }
        }else { //랭킹에 저장된 정보가 없음
            gameRankResponseDTO = GameRankResponseDTO.builder()
                    .userId(userInfo.getUserId())
                    .userName(userInfo.getUserName())
                    .score(dto.getScore())
                    .recordDate(LocalDateTime.now())
                    .build();
        }
        // DB에 저장된 스코어보다 새로 저장될 스코어가 클 경우
        csGameRankRepository.save(gameRankResponseDTO.csToEntity(user));
        return findRank();

    }

}
