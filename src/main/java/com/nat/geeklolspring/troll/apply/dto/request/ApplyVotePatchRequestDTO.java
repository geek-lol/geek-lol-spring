package com.nat.geeklolspring.troll.apply.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyVotePatchRequestDTO {
    // 좋아요를 수정하는데 필요한 정보들
    private Long applyId;
}
