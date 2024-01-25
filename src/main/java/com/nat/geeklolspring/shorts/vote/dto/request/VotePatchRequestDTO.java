package com.nat.geeklolspring.shorts.vote.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VotePatchRequestDTO {
    private String receiver;
    private Long shortsId;
}
