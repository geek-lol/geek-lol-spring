package com.nat.geeklolspring.board.bulletin.repository;

import com.nat.geeklolspring.entity.BoardBulletin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardBulletinRepository extends JpaRepository<BoardBulletin,String> {
}
