package com.nat.geeklolspring.shorts.shortsreply.repository;

import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.entity.ShortsReply;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShortsReplyRepository extends JpaRepository<ShortsReply, Long> {

    // ShortsId에 달린 모든 댓글을 가져오는걸 JPA로 자동화해서 만든 코드
    Page<ShortsReply> findAllByShortsIdOrderByReplyDateDesc(BoardShorts shortsId, Pageable pageable);

    List<ShortsReply> findAllByShortsId(BoardShorts shortsId);

    //특정 아이디가 쓴 댓글을 조회
    Page<ShortsReply> findAllByWriterId(User writerId, Pageable pageable);
    //내꺼 갯수 반환
    int countByWriterId(User writerId);
}
