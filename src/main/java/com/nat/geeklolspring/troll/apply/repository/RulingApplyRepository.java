package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.time.LocalDateTime;
import java.util.List;

public interface RulingApplyRepository extends JpaRepository<BoardApply,Long> {

    // 등록일이 startDate endDate 사이에서 upCount가 가장 높은 BoardApply 반환  (upCount가 같을경우는 댓글수가 많은 것)
    BoardApply findFirstByApplyDateBetweenOrderByUpCountDescViewCountDesc(LocalDateTime startDate, LocalDateTime endDate);
    //최신글 정렬
    Page <BoardApply> findAllByOrderByApplyDateDesc(Pageable pageable);
    //인기글 정렬
    Page <BoardApply> findAllByOrderByUpCountDesc(Pageable pageable);

    //제목 찾기
    Page<BoardApply> findByTitleContaining(String keyword,Pageable pageable);
    //작성자 찾기
    @Query("SELECT b FROM BoardApply b WHERE b.userId.userName Like %:keyword%")
    Page<BoardApply> findBoardsByUserName(String keyword, Pageable pageable);
    //제목+내용 찾기
    Page<BoardApply> findByTitleContainingAndContentContaining(String keyword,String conkeyword,Pageable pageable);

    Page<BoardApply> findByUserId(User user, Pageable pageable);

    //내꺼 갯수 반환
    int countByUserId(User user);

    @Modifying
    @Query("update BoardApply s set s.upCount = s.upCount + 1 where s.applyId = :applyId")
    void plusUpCount(Long applyId);

    @Modifying
    @Query("update BoardApply s set s.upCount = s.upCount - 1 where s.applyId = :applyId")
    void downUpCount(Long applyId);


}
