package com.nat.geeklolspring.lolapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameDataResponseDTO {
    private int gameDuration;
    private Long gameStartTimestamp;
    private Long gameEndTimestamp;
    private int queueId;
    private Long gameId;
    private String matchId;

}
