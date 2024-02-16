package com.nat.geeklolspring.game.repository;

import com.nat.geeklolspring.entity.CsGameRank;
import com.nat.geeklolspring.entity.ResGameRank;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CsGameRankRepository extends JpaRepository<CsGameRank,Long> {
    List<CsGameRank> findAllByOrderByScoreDesc();
    CsGameRank findByUserId(String id);

    void deleteAllByUser(User user);
}
