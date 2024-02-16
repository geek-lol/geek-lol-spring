package com.nat.geeklolspring.lolapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class RankingResponseDTO {

    private String tier;
    private String queue;
    private String name;
    private List<RankingEntryDTO> entries;


    private List<SummonerAccount> summonerAccount;
    private List<SummonerInfo> summonerInfo;



    @Data
    @NoArgsConstructor
    public static class RankingEntryDTO {
        private String summonerId;
        private String summonerName;
        private int leaguePoints;
        private int wins;
        private int losses;
    }

    @Data
    @NoArgsConstructor
    public static class SummonerInfo {
        private int profileIconId;
        private long summonerLevel;
        private String puuid;
    }

    @Data
    @NoArgsConstructor
    public static class SummonerAccount {
        private String gameName;
        private String tagLine;
        private String puuid;
    }

}
