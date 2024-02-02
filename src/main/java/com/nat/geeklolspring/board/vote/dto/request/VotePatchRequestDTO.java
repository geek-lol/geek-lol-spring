package com.nat.geeklolspring.board.vote.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotePatchRequestDTO {
    // 좋아요를 수정하는데 필요한 정보들
    private Long shortsId;
}
