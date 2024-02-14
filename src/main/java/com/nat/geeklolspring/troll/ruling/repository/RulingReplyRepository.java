package com.nat.geeklolspring.troll.ruling.repository;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingReply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RulingReplyRepository extends JpaRepository<RulingReply,Long> {

    Page<RulingReply> findAllByRulingId(Long boardRuling, Pageable pageable);
    Page<RulingReply> findByWriterId(String id, Pageable pageable);

    //게시글 별 댓글조회
    int countByRulingId(Long id);
    //내꺼 갯수 반환
    int countByWriterId(String id);

}
