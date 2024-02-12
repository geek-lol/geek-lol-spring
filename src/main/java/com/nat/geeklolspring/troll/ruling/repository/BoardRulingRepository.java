package com.nat.geeklolspring.troll.ruling.repository;

import com.nat.geeklolspring.entity.BoardRuling;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRulingRepository extends JpaRepository<BoardRuling, Long> {

    Page<BoardRuling> findAllByOrderByRulingDateDesc(Pageable pageable);
    List<BoardRuling> findAllByOrderByRulingDateDesc();

    //내꺼 조회
    Page<BoardRuling> findAllByRulingPosterId(String id, Pageable pageable);

}
