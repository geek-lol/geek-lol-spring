package com.nat.geeklolspring.troll.apply.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyReplyUpdateRequestDTO {
    // update(수정)시 필요한 정보들
    private Long replyId;
    private String context;
}
