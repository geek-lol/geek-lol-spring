package com.nat.geeklolspring.shorts.vote.repository;

import com.nat.geeklolspring.entity.VoteCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteCheckRepository extends JpaRepository<VoteCheck, Long> {
    VoteCheck findByShortsIdAndReceiver(Long shortsId, String receiver);
}
