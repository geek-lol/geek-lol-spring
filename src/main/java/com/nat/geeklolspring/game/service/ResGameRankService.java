package com.nat.geeklolspring.game.service;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.ResGameRank;
import com.nat.geeklolspring.game.dto.request.GameRankRequestDTO;
import com.nat.geeklolspring.game.dto.response.GameRankListResponseDTO;
import com.nat.geeklolspring.game.dto.response.GameRankResponseDTO;
import com.nat.geeklolspring.game.repository.ResGameRankRepository;
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
public class ResGameRankService {
    private final ResGameRankRepository resGameRankRepository;

    // 랭킹 조회
    public GameRankListResponseDTO findRank(){
        List<ResGameRank> rankList = resGameRankRepository.findAll();
        List<GameRankResponseDTO> dtoList = rankList.stream()
                .map(GameRankResponseDTO::new)
                .collect(Collectors.toList());

        return GameRankListResponseDTO.builder()
                .gameRankList(dtoList)
                .build();
    }
    // 랭킹 저장
    public GameRankListResponseDTO addRank(GameRankRequestDTO dto, TokenUserInfo userInfo){
        // 근데 랭킹에 이미 있고, 원래 점수보다 높으면 수정해서 저장
        ResGameRank userRankInfo = resGameRankRepository.findByUserId(userInfo.getUserId());
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
        resGameRankRepository.save(gameRankResponseDTO.toEntity());
        return findRank();

    }

}
