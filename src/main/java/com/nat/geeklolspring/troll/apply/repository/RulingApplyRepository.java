package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.BoardApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RulingApplyRepository extends JpaRepository<BoardApply,Long> {
}