package com.nat.geeklolspring.troll.service;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.troll.repository.RulingApplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RulingApplyService {
    private final RulingApplyRepository rar;

    // 게시물 저장
    public void createBoard(BoardRuling boardRuling){
        rar.save(boardRuling).;
    }

    // 게시물 수정

    // 게시물 삭제

    // 기준일로 부터 3일 뒤 추천수 많은거 골라내기

    //게시물 추천수 증감



}
