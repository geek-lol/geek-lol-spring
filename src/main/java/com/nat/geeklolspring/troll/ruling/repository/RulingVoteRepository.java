package com.nat.geeklolspring.troll.ruling.repository;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RulingVoteRepository extends JpaRepository<RulingCheck,Long> {

    //투표 게시물 총 찬성표 갯수
    int countByRulingIdAndPros(Long rulingId, int num);
    // 총 반대표 갯수
    int countByRulingIdAndCons(Long rulingId, int num);
}
