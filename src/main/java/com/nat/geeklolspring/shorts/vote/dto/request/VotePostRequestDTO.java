package com.nat.geeklolspring.shorts.vote.dto.request;

import com.nat.geeklolspring.entity.VoteCheck;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotePostRequestDTO {
    private String receiver;
    private Long shortsId;
    private int up;

    public VoteCheck toEntity(VotePostRequestDTO dto) {
        return VoteCheck.builder()
                .receiver(dto.receiver)
                .shortsId(dto.shortsId)
                .up(this.up)
                .build();
    }
}
