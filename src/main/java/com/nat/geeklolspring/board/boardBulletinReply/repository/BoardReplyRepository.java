package com.nat.geeklolspring.board.boardBulletinReply.repository;

import com.nat.geeklolspring.entity.BoardReply;
import com.nat.geeklolspring.entity.ShortsReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    // ShortsId에 달린 모든 댓글을 가져오는걸 JPA로 자동화해서 만든 코드
    Page<BoardReply> findAllByBulletinId(Long bulletinId, Pageable pageable);
}
