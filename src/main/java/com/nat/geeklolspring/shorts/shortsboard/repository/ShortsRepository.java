package com.nat.geeklolspring.shorts.shortsboard.repository;

import com.nat.geeklolspring.entity.BoardShorts;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShortsRepository extends JpaRepository<BoardShorts, Long> {
    Page<BoardShorts> findByTitleContaining(String title, Pageable pageable);
    @Modifying
    @Query("update BoardShorts s set s.upCount = s.upCount + 1 where s.shortsId = :shortsId")
    void plusUpCount(Long shortsId);

    @Modifying
    @Query("update BoardShorts s set s.upCount = s.upCount - 1 where s.shortsId = :shortsId")
    void downUpCount(Long shortsId);

    @Modifying
    @Query("update BoardShorts s set s.viewCount = s.viewCount + 1 where s.shortsId = :shortsId")
    void upViewCount(Long shortsId);

    @Modifying
    @Query("update BoardShorts s set s.replyCount = s.replyCount + 1 where s.shortsId = :shortsId")
    void upReplyCount(Long shortsId);

    @Modifying
    @Query("update BoardShorts s set s.replyCount = s.replyCount - 1 where s.shortsId = :shortsId")
    void downReplyCount(Long shortsId);

    BoardShorts findByShortsId(Long id);

    Page<BoardShorts> findAllByUploaderId(String id,Pageable pageable);
}
