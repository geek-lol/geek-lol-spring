package com.nat.geeklolspring.board.vote.dto.response;

import com.nat.geeklolspring.entity.BulletinCheck;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardVoteResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private int up;

    public BoardVoteResponseDTO(BulletinCheck dto) {
        this.up = dto.getGood();
    }
}
