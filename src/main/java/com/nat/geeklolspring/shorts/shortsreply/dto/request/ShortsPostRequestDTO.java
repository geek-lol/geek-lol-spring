package com.nat.geeklolspring.shorts.shortsreply.dto.request;

import com.nat.geeklolspring.entity.BoardShorts;
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
public class ShortsPostRequestDTO {
    // Post 시 필요한 정보들
    private String context;

    public ShortsReply toEntity(User user, BoardShorts boardShorts) {
        return ShortsReply.builder()
                .shortsId(boardShorts)
                .writerId(user)
                .context(context)
                .build();
    }
}
