package com.nat.geeklolspring.game.repository;

import com.nat.geeklolspring.entity.CsGameRank;
import com.nat.geeklolspring.entity.ResGameRank;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CsGameRankRepository extends JpaRepository<CsGameRank,Long> {
    CsGameRank findByUserId(String id);
}
