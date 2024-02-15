package com.nat.geeklolspring.board.boardBulletinReply.dto.request;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BoardReply;
import com.nat.geeklolspring.entity.ShortsReply;
import com.nat.geeklolspring.entity.User;
import lombok.*;

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

    public BoardReply toEntity(BoardBulletin bulletin, User user) {
        return BoardReply.builder()
                .bulletin(bulletin)
                .writerUser(user)
                .replyText(context)
                .build();
    }

}
