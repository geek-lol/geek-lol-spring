package com.nat.geeklolspring.shorts.shortsreply.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortsUpdateRequestDTO {
    // update(수정)시 필요한 정보들
    private Long shortsId;
    private Long replyId;
    private String context;
}
