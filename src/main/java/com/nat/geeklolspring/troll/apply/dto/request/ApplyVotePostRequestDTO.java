package com.nat.geeklolspring.troll.apply.dto.request;

import com.nat.geeklolspring.entity.VoteApply;
import com.nat.geeklolspring.entity.VoteCheck;
import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVotePostRequestDTO {
    // 좋아요를 만들 때 필요한 정보들
    @NotNull
    private Long applyId;

    public VoteApply toEntity(ApplyVotePostRequestDTO dto) {
        return VoteApply.builder()
                .applyId(dto.applyId)
                .build();
    }
}
