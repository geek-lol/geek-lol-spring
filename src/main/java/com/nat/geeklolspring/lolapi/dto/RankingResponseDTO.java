package com.nat.geeklolspring.lolapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RankingResponseDTO {

    private String tier;
    private String rank;
    private String summonerId;
    private int leaguePoints;
    private int wins;
    private int losses;


    private SummonerAccount summonerAccount;
    private SummonerInfo summonerInfo;


    @Data
    public static class SummonerAccount {
        private String gameName;
        private String tagLine;
        private String puuid;
    }

    @Data
    public static class SummonerInfo {
        private int profileIconId;
        private long summonerLevel;
    }

}
