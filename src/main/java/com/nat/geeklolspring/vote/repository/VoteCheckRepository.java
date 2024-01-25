package com.nat.geeklolspring.vote.repository;

import com.nat.geeklolspring.entity.VoteCheck;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoteCheckRepository extends JpaRepository<VoteCheck, Long> {
    VoteCheck findByShortsIdAndReceiver(Long shortsId, String receiver);
}
