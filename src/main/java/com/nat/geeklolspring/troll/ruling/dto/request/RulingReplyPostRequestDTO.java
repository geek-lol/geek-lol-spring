package com.nat.geeklolspring.troll.ruling.dto.request;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingReply;
import com.nat.geeklolspring.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RulingReplyPostRequestDTO {
    // Post 시 필요한 정보들
    private String context;

    public RulingReply toEntity(BoardRuling boardRuling, User user) {
        return RulingReply.builder()
                .rulingId(boardRuling)
                .context(context)
                .rulingWriterId(user)
                .build();
    }
}
