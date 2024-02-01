package com.nat.geeklolspring.shorts.vote.dto.response;

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
    // 웹사이트에 보내줄(보여줄) 정보들
    private int up;

    public VoteResponseDTO(VoteCheck dto) {
        this.up = dto.getUp();
    }
}
