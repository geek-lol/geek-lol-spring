package com.nat.geeklolspring.shorts.vote.dto.request;

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
public class VotePostRequestDTO {
    // 좋아요를 만들 때 필요한 정보들
    @NotNull
    private String receiver;

    @NotNull
    private Long shortsId;

    public VoteCheck toEntity(VotePostRequestDTO dto) {
        return VoteCheck.builder()
                .receiver(dto.receiver)
                .shortsId(dto.shortsId)
                .build();
    }
}
