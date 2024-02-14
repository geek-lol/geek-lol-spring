package com.nat.geeklolspring.board.boardBulletinReply.dto.request;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BoardReply;
import lombok.*;

import java.util.Optional;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardPostRequestDTO {
    // Post 시 필요한 정보들
    private String context;

    public BoardReply toEntity(BoardBulletin boardBulletinId) {
        return BoardReply.builder()
                .boardBulletinId(boardBulletinId)
                .replyText(context)
                .build();
    }

}
