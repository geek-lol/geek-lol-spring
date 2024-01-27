package com.nat.geeklolspring.troll.repository;

import com.nat.geeklolspring.entity.RulingCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RulingVoteRepository extends JpaRepository<RulingCheck,Long> {
}
