package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.BoardApply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RulingApplyRepository extends JpaRepository<BoardApply,Long> {
    BoardApply findFirstByApplyDateBetweenOrderByUpCountDescReportCountDesc(LocalDateTime startDate, LocalDateTime endDate);
}
