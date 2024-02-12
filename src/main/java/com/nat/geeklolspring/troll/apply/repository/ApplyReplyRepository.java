package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.ShortsReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyReplyRepository extends JpaRepository<ApplyReply,Long> {
    Page<ApplyReply> findAllByApplyId(Long id, Pageable pageable);

    int countByApplyId(Long id);

    //내가 쓴 댓글 전체조회
    Page<ApplyReply> findByWriterId(String id,Pageable pageable);
}
