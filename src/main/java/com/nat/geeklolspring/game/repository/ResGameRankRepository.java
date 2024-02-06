package com.nat.geeklolspring.game.repository;

import com.nat.geeklolspring.entity.ResGameRank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResGameRankRepository extends JpaRepository<ResGameRank,Long> {
    List<ResGameRank> findAllByOrderByScore();
    ResGameRank findByUserId(String id);
}
