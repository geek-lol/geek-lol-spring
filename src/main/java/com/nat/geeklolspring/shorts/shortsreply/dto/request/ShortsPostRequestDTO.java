package com.nat.geeklolspring.shorts.shortsreply.dto.request;

import com.nat.geeklolspring.entity.ShortsReply;
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
    private String writerId;
    private String context;

    public ShortsReply toEntity(Long id) {
        return ShortsReply.builder()
                .shortsId(id)
                .writerId(writerId)
                .context(context)
                .build();
    }
}
