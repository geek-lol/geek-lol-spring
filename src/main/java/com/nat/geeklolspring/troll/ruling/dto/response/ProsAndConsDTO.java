package com.nat.geeklolspring.troll.ruling.dto.response;

import lombok.*;

@Setter @Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProsAndConsDTO {
    //찬성 투표 갯수
    private float pros;
    //반대 투표 갯수
    private float cons;

    //찬성 퍼센트율
    private float prosPercent;
    //반대 퍼센트율
    private float consPercent;

}
