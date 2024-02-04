package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.BoardApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RulingApplyRepository extends JpaRepository<BoardApply,Long> {

    // 등록일이 startDate endDate 사이에서 upCount가 가장 높은 BoardApply 반환  (upCount가 같을경우는 댓글수가 많은 것)
    BoardApply findFirstByApplyDateBetweenOrderByUpCountDescReportCountDesc(LocalDateTime startDate, LocalDateTime endDate);


}
