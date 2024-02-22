package com.nat.geeklolspring.lolapi.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TeamDataResponseDTO {
    private List<BanDTO> bans;
    private ObjectivesDTO objectives;
    private int teamId;
    private boolean win;
    private int queueId;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BanDTO {
        private int championId;
        private int pickTurn;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectivesDTO {
        @JsonProperty("baron")
        private ObjectiveDTO baron;
        @JsonProperty("champion")
        private ObjectiveDTO champion;
        @JsonProperty("dragon")
        private ObjectiveDTO dragon;
        @JsonProperty("horde")
        private ObjectiveDTO horde;
        @JsonProperty("inhibitor")
        private ObjectiveDTO inhibitor;
        @JsonProperty("riftHerald")
        private ObjectiveDTO riftHerald;
        @JsonProperty("tower")
        private ObjectiveDTO tower;


    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ObjectiveDTO {
        private boolean first;
        private int kills;
    }

}
