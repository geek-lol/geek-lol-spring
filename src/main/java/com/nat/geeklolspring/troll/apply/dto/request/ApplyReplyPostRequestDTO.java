package com.nat.geeklolspring.troll.apply.dto.request;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.BoardApply;
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
public class ApplyReplyPostRequestDTO {
    // Post 시 필요한 정보들
    private String context;

    public ApplyReply toEntity(BoardApply apply, User user) {
        return ApplyReply.builder()
                .context(context)
                .applyId(apply)
                .userId(user)
                .build();
    }
}
