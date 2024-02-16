package com.nat.geeklolspring.lolapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class CurrentGameInfo {
    private Long gameId;
    private String gameType;
    private Long gameStartTime;
    private Long mapId;
    private Long gameLength;
    private String platformId;
    private String gameMode;
    private List<BannedChampion> bannedChampions;
    private Long gameQueueConfigId;
    private Observer observers;
    private List<CurrentGameParticipant> participants;
    private List<AllSummoner> allSummoners;

    @Data
    public static class AllSummoner {
        private String gameName;
        private String tagLine;
        private String puuid;
    }

    @Data
    public static class BannedChampion {
        private int pickTurn;
        private Long championId;
        private Long teamId;
    }

    @Data
    public static class Observer {
        private String encryptionKey;
    }

    @Data
    public static class CurrentGameParticipant {
        private Long championId;
        private Perks perks;
        private Long profileIconId;
        private boolean bot;
        private Long teamId;
        private String summonerName;
        private String summonerId;
        private String puuid;
        private Long spell1Id;
        private Long spell2Id;
        private String gameName;
        private String tagLine;
        private List<GameCustomizationObject> gameCustomizationObjects;
    }

    @Data
    public static class Perks {
        private List<Long> perkIds;
        private Long perkStyle;
        private Long perkSubStyle;
    }

    @Data
    public static class GameCustomizationObject {
        private String category;
        private String content;
    }
}