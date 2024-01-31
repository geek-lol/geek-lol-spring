package com.nat.geeklolspring.board.bulletin.repository;

import com.nat.geeklolspring.board.bulletin.dto.response.BoardBulletinDetailResponseDTO;
import com.nat.geeklolspring.entity.BoardBulletin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BoardBulletinRepository extends JpaRepository<BoardBulletin,Long> {

//    @Modifying
//    @Query("delete from BoardBulletin s WHERE s.bulletinId=?1")
//    void deleteByBoardBulletinIdWithJPQL(Long BulletinId);
}
