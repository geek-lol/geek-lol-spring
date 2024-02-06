package com.nat.geeklolspring.troll.apply.dto.request;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.ShortsReply;
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

    public ApplyReply toEntity(Long id) {
        return ApplyReply.builder()
                .applyId(id)
                .context(context)
                .build();
    }
}
