package com.nat.geeklolspring.lolapi.controller;

import com.nat.geeklolspring.lolapi.dto.CurrentGameInfo;
import com.nat.geeklolspring.lolapi.dto.RankingResponseDTO;
import com.nat.geeklolspring.lolapi.dto.response.AllChampionMasteryResponseDTO;
import com.nat.geeklolspring.lolapi.dto.response.ChampionMasteryResponseDTO;
import com.nat.geeklolspring.lolapi.services.LolApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class LolApiCotroller {

    private final LolApiService lolApiService;

    @GetMapping("/find/{name}/{tag}")
    public JSONObject findUserInfomation(
            @PathVariable(name = "name") String name,
            @PathVariable(name = "tag") String tag
    ) {
        JSONObject userInfomation = lolApiService.findUserInfomation(name, tag);
        log.warn("유저정보 : {}", userInfomation);
        if (userInfomation != null) {
            return userInfomation;
        } else {
            return null;
        }
    }

    @GetMapping(path = "/recentGames/{start}/{count}")
    public ResponseEntity<List<Map<String, Object>>> loadRecentGames(@PathVariable(name = "start") int start,
                                                                     @PathVariable(name = "count") int count
    ) throws Exception {
        List<Map<String, Object>> recentGames = lolApiService.getRecentGames(start, count);
        //log.error("recentGames : {}", recentGames);
        return ResponseEntity.ok().body(recentGames);
    }

    @GetMapping(path = "/championMastery")
    public ResponseEntity<?> loadChampionMastery() {
        log.info("load champion mastery get!!");
        List<ChampionMasteryResponseDTO> championMastery = lolApiService.getChampionMastery();
        return ResponseEntity.ok().body(championMastery);
    }

    @GetMapping("/all-champion-mastery")
    public ResponseEntity<?> loadAllChampionMastery() {
        log.info("load all champion mastery");
        List<AllChampionMasteryResponseDTO> allChampionMasteryResponseDTOS = lolApiService.getAllChampionMastery();
        return ResponseEntity.ok().body(allChampionMasteryResponseDTOS);
    }

    @GetMapping("/realtimeGame")
    public ResponseEntity<?> loadRealtimeGame() {
        log.info("load realtime game get!");
        CurrentGameInfo realtimeGame = lolApiService.getRealtimeGame();
        return ResponseEntity.ok().body(realtimeGame);
    }

    @GetMapping("/ranking/{tier}")
    public ResponseEntity<?> loadRanking(@PathVariable String tier) {
        log.info("load ranking info get");
        RankingResponseDTO rankingData = lolApiService.getRankingData(tier);
        log.warn("ranking data : {}", rankingData);
        return ResponseEntity.ok().body(rankingData);
    }
}
