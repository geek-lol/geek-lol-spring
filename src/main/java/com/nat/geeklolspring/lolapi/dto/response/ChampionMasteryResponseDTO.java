package com.nat.geeklolspring.lolapi.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ChampionMasteryResponseDTO {
    private int championId;
    private int championLevel;
    private int championPoints;
    private long lastPlayTime;
}
