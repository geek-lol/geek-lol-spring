package com.nat.geeklolspring.board.vote.repository;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BulletinCheck;
import com.nat.geeklolspring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardVoteCheckRepository extends JpaRepository<BulletinCheck, Long> {
    // JPA에서 자동으로 만들어주는 메서드를 사용해서 만든
    // shortsId와 receiver(계정명)으로 VoteCheck 정보 찾기
    BulletinCheck findByBulletinAndUser(BoardBulletin bulletin, User user);
}
