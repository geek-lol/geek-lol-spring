package com.nat.geeklolspring.board.boardBulletinReply.repository;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BoardReply;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardReplyRepository extends JpaRepository<BoardReply, Long> {

    // ShortsId에 달린 모든 댓글을 가져오는걸 JPA로 자동화해서 만든 코드
    Page<BoardReply> findAllByBulletinOrderByBoardReplyDate(BoardBulletin bulletin, Pageable pageable);

    //해당 회원의 댓글 조회 하는 기능
    Page<BoardReply> findAllByWriterUser(User writerUser, Pageable pageable);


    //내꺼 갯수 반환
    int countByWriterUser(User writerUser);

    //해당 작성자의 댓글을 삭제
    void deleteAllByWriterUser(User writerId);
}
