package com.nat.geeklolspring.shorts.vote.repository;

import com.nat.geeklolspring.entity.VoteCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCheckRepository extends JpaRepository<VoteCheck, Long> {
    // JPA에서 자동으로 만들어주는 메서드를 사용해서 만든
    // shortsId와 receiver(계정명)으로 VoteCheck 정보 찾기
    VoteCheck findByShortsIdAndReceiver(Long shortsId, String receiver);
}
