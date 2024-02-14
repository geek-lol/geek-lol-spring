package com.nat.geeklolspring.troll.ruling.repository;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRulingRepository extends JpaRepository<BoardRuling, Long> {

    @Query("SELECT r FROM BoardRuling r ORDER BY r.ApplyId.applyDate DESC ")
    Page<BoardRuling> findAllByOrderByRulingDateDescPageable(Pageable pageable);

    @Query("SELECT r FROM BoardRuling r ORDER BY r.ApplyId.applyDate DESC ")
    List<BoardRuling> findAllByOrderByRulingDateDesc();


    //내꺼 조회
    Page<BoardRuling> findAllByRulingPosterId(User user, Pageable pageable);
    //내꺼 갯수 반환
    int countByRulingPosterId(User user);

}
