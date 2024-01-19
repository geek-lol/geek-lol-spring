package com.nat.geeklolspring.vote.dto.response;

import com.nat.geeklolspring.entity.VoteCheck;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoteResponseDTO {
    private int up;
    private int down;

    public VoteResponseDTO(VoteCheck dto) {
        this.up = dto.getUp();
        this.down = dto.getDown();
    }
}
