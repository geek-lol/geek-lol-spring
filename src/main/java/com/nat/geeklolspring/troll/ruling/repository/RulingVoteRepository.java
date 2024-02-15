package com.nat.geeklolspring.troll.ruling.repository;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingCheck;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RulingVoteRepository extends JpaRepository<RulingCheck,Long> {

    //투표 게시물 총 찬성표 갯수
    int countByRulingIdAndPros(BoardRuling rulingId, int pros);
    // 총 반대표 갯수
    int countByRulingIdAndCons(BoardRuling rulingId, int cons);

    //유저로 db에 있는지 확인하는 쿼리
    boolean existsByRulingVoter(User user);
}
