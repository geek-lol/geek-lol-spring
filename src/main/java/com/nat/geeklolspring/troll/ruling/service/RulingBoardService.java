package com.nat.geeklolspring.troll.ruling.service;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.troll.ruling.dto.response.CurrentBoardListResponseDTO;
import com.nat.geeklolspring.troll.ruling.dto.response.RulingBoardDetailResponseDTO;
import com.nat.geeklolspring.troll.ruling.repository.BoardRulingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class RulingBoardService {
    private final BoardRulingRepository boardRulingRepository;

    // 최근 게시물 2건 조회
    public CurrentBoardListResponseDTO descRulingBoard(){
        //투표 게시판의 목록을 불러옴
        List<BoardRuling> topTwo = boardRulingRepository.findTopByOrderByRulingDateDesc();

        //목록이 비어있거나 1개인 경우
        if (topTwo.isEmpty()){
            return CurrentBoardListResponseDTO.builder()
                    .currentBoard(null)
                    .previousBoard(null)
                    .build();
        }else if (topTwo.size() == 1){
            return CurrentBoardListResponseDTO.builder()
                    .currentBoard(new RulingBoardDetailResponseDTO(topTwo.get(0)))
                    .previousBoard(null)
                    .build();
        }

        //최근 2개의 게시판만 저장해서 리턴
        return new CurrentBoardListResponseDTO(topTwo);
    }


}