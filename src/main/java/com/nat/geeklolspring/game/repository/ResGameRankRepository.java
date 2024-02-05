package com.nat.geeklolspring.game.repository;

import com.nat.geeklolspring.entity.ResGameRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResGameRankRepository extends JpaRepository<ResGameRank,Long> {
    ResGameRank findByUserId(String id);
}
