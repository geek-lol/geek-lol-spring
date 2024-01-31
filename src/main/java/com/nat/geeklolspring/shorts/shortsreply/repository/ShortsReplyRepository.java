package com.nat.geeklolspring.shorts.shortsreply.repository;

import com.nat.geeklolspring.entity.ShortsReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShortsReplyRepository extends JpaRepository<ShortsReply, Long> {

    // ShortsId에 달린 모든 댓글을 가져오는걸 JPA로 자동화해서 만든 코드
    List<ShortsReply> findAllByShortsId(Long shortsId);

    Page<ShortsReply> getAllByShortsId(Pageable pageable, Long shortsId);
}
