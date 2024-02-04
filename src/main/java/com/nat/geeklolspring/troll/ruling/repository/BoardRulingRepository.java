package com.nat.geeklolspring.troll.ruling.repository;

import com.nat.geeklolspring.entity.BoardRuling;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRulingRepository extends JpaRepository<BoardRuling, Long> {

    List<BoardRuling> findTopByOrderByRulingDateDesc();
}
