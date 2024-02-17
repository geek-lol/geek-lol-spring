package com.nat.geeklolspring.troll.ruling.repository;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingReply;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RulingReplyRepository extends JpaRepository<RulingReply,Long> {

    Page<RulingReply> findAllByRulingId(BoardRuling boardRuling, Pageable pageable);
    Page<RulingReply> findByRulingWriterId(User user, Pageable pageable);

    //게시글 별 댓글조회
    int countByRulingId(BoardRuling boardRuling);
    //내꺼 갯수 반환
    int countByRulingWriterId(User user);
    //해당 작성자의 댓글을 삭제
    void deleteAllByRulingWriterId(User writerId);
}
