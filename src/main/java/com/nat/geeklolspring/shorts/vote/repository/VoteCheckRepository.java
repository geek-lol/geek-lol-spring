package com.nat.geeklolspring.shorts.vote.repository;

import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.entity.VoteCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCheckRepository extends JpaRepository<VoteCheck, Long> {
    // JPA에서 자동으로 만들어주는 메서드를 사용해서 만든
    // shortsId와 receiver(계정명)으로 VoteCheck 정보 찾기
    VoteCheck findByShortsAndUser(BoardShorts shorts, User user);
    //해당 작성자의 좋아요를 삭제
    void deleteAllByUser(User writerId);
}
