package com.nat.geeklolspring.game.dto.request;

import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRankRequestDTO {
    private float score;
}
