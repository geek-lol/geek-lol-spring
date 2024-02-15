package com.nat.geeklolspring.game.repository;

import com.nat.geeklolspring.entity.ResGameRank;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResGameRankRepository extends JpaRepository<ResGameRank,Long> {
    List<ResGameRank> findAllByOrderByScore();
    ResGameRank findByUser(User user);

    void deleteAllByUser(User user);
}
