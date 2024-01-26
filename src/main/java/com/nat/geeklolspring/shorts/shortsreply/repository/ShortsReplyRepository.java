package com.nat.geeklolspring.shorts.shortsreply.repository;

import com.nat.geeklolspring.entity.ShortsReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShortsReplyRepository extends JpaRepository<ShortsReply, Long> {
    List<ShortsReply> findAllByShortsId(Long shortsId);
}
