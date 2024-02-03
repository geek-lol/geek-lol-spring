package com.nat.geeklolspring.troll.apply.dto.response;

import com.nat.geeklolspring.entity.VoteApply;
import com.nat.geeklolspring.entity.VoteCheck;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVoteResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private int up;

    public ApplyVoteResponseDTO(VoteApply dto) {
        this.up = dto.getUp();
    }
}
