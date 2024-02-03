package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.VoteApply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplyVoteCheckRepository extends JpaRepository<VoteApply, Long> {
    // JPA에서 자동으로 만들어주는 메서드를 사용해서 만든
    // shortsId와 receiver(계정명)으로 VoteCheck 정보 찾기
    VoteApply findByApplyIdAndReceiver(Long applyId, String receiver);
}
