package com.nat.geeklolspring.troll.ruling.repository;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RulingReplyRepository extends JpaRepository<RulingReply,Long> {

    List<RulingReply> findAllByRulingId(BoardRuling boardRuling);
}
