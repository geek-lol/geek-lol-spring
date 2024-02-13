package com.nat.geeklolspring.lolapi.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AllChampionMasteryResponseDTO {

    private long championId;
    private int championLevel;
    private int championPoints;
    private long lastPlayTime;

    private boolean chestGranted;
}
