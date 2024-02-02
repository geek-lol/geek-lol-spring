package com.nat.geeklolspring.troll.dto.response;

import lombok.*;

@Setter @Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProsAndConsDTO {
    private float pros;
    private float cons;

}
