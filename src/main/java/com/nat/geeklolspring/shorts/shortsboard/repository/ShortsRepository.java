package com.nat.geeklolspring.shorts.shortsboard.repository;

import com.nat.geeklolspring.entity.BoardShorts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsRepository extends JpaRepository<BoardShorts, Long> {

}
