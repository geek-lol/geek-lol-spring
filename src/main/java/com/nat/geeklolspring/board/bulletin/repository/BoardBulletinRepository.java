package com.nat.geeklolspring.board.bulletin.repository;

import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.entity.BoardBulletin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardBulletinRepository extends JpaRepository<BoardBulletin,Long> {
    Page<BoardBulletin> findByTitleContaining(String title, Pageable pageable);
}
