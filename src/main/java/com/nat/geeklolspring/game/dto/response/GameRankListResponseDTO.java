package com.nat.geeklolspring.game.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRankListResponseDTO {
    private String error;
    private List<GameRankResponseDTO> gameRankList;
}
