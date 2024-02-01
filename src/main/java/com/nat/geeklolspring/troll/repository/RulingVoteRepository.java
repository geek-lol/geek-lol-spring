package com.nat.geeklolspring.troll.repository;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RulingVoteRepository extends JpaRepository<RulingCheck,Long> {

    //특정 게시물 총 찬성표 갯수
    @Query("SELECT count(rc.one) from  RulingCheck rc where rc.one = 1 AND rc.rulingId = ?1")
    int getPros(BoardRuling br);

    //특정 게시물 총 반대표 갯수
    @Query("SELECT count(rc.two) from  RulingCheck rc where rc.two = 1 AND rc.rulingId = ?1")
    int getCons(BoardRuling br);
}
