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
    private Long shortsId;
    private Long replyId;
    private String context;
}
