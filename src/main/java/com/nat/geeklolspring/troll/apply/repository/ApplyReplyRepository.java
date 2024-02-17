package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.ShortsReply;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyReplyRepository extends JpaRepository<ApplyReply,Long> {
    Page<ApplyReply> findAllByApplyId(BoardApply applyId, Pageable pageable);

    //내가 쓴 댓글 전체조회
    Page<ApplyReply> findByUserId(User writerId, Pageable pageable);

    //게시판별 댓글 수 조회
    int countByApplyId(BoardApply applyId);
    //내꺼 갯수 반환
    int countByUserId(User userId);

    //해당 작성자의 댓글을 삭제
    void deleteAllByUserId(User writerId);
}
