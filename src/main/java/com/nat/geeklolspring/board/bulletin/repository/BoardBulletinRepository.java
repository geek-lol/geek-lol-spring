package com.nat.geeklolspring.board.bulletin.repository;

import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.entity.BoardBulletin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardBulletinRepository extends JpaRepository<BoardBulletin,Long> {
    Page<BoardBulletin> findByTitleContainingOrderByBoardDateDesc(String title, Pageable pageable);

    Page<BoardBulletin> findAllByOrderByBoardDateDesc(Pageable pageable);

    Page<BoardBulletin> findByPosterIdContainingOrderByBoardDateDesc(String posterKeyword, Pageable pageable);

    Page<BoardBulletin> findByBoardContentContainingOrderByBoardDateDesc(String contentKeyword, Pageable pageable);

    @Modifying
    @Query("update BoardBulletin s set s.upCount = s.upCount + 1 where s.bulletinId = :bulletinId")
    void plusUpCount(Long bulletinId);

    @Modifying
    @Query("update BoardBulletin s set s.upCount = s.upCount - 1 where s.bulletinId = :bulletinId")
    void downUpCount(Long bulletinId);



}
