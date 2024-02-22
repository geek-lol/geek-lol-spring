package com.nat.geeklolspring.troll.apply.repository;

import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.User;
import com.nat.geeklolspring.entity.VoteApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ApplyVoteCheckRepository extends JpaRepository<VoteApply, Long> {
    // JPA에서 자동으로 만들어주는 메서드를 사용해서 만든
    // shortsId와 receiver(계정명)으로 VoteCheck 정보 찾기
    VoteApply findByApplyIdAndReceiver(BoardApply applyId, User receiver);

    // applyId가 같은 것의 열 갯수를 반환
    int countByApplyId(BoardApply applyId);

    //해당 작성자의 좋아요를 삭제
    void deleteAllByReceiver(User writerId);

}
