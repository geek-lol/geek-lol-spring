package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.ShortsReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyReplyRepository extends JpaRepository<ApplyReply,Long> {
    Page<ApplyReply> findAllByApplyId(Long shortsId, Pageable pageable);
}
