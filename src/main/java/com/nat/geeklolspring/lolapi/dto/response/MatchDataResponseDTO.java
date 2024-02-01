package com.nat.geeklolspring.lolapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDataResponseDTO {
    private int teamId;
    private String summonerId;
    private String riotIdGameName;
    private String riotIdTagline;
    private int summoner1Id;
    private int summoner2Id;
    private String teamPosition;
    private int championId;
    private int kills;
    private int deaths;
    private int assists;
    private int neutralMinionsKilled;
    private int totalMinionsKilled;
    private int totalDamageDealtToChampions;

    private String championName;


    private int item0;
    private int item1;
    private int item2;
    private int item3;
    private int item4;
    private int item5;
    private int item6;

    private PerksDataResponseDTO perks;

    private String allInPings;

    private boolean win;

}
