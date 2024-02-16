package com.nat.geeklolspring.lolapi.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nat.geeklolspring.lolapi.dto.CurrentGameInfo;
import com.nat.geeklolspring.lolapi.dto.RankingResponseDTO;
import com.nat.geeklolspring.lolapi.dto.response.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

import static com.nat.geeklolspring.lolapi.dto.CurrentGameInfo.AllSummoner;
import static com.nat.geeklolspring.lolapi.dto.CurrentGameInfo.CurrentGameParticipant;

@Service
@Slf4j
@RequiredArgsConstructor
public class LolApiService {
    @Value("${lol.app-key}")
    private String appKey;
    private final String riotUrl = "https://kr.api.riotgames.com";
    private final String aisaRiotUrl = "https://asia.api.riotgames.com";
    String puuid = "";
    String infoId = "";


    private final WebClient.Builder webClientBuilder;


    public JSONObject getJsonToApi(String requestURL) {
        try {
            Mono<String> responseMono = webClientBuilder.build()
                    .get()
                    .uri(requestURL)
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8")
                    .header("Origin", "https://developer.riotgames.com")
                    .retrieve()
                    .bodyToMono(String.class);

            String responseBody = responseMono.block(); // Blocking for simplicity

            JSONParser jParser = new JSONParser();
            return (JSONObject) jParser.parse(responseBody);
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                log.error("Failed to get data: HTTP 404 Not Found");
                return null;
            } else {
                log.error("response is error : {}", e.getRawStatusCode());
                return null;  // Or potentially throw a custom exception here
            }
        } catch (Exception e) {
            log.error("An error occurred while making the HTTP request", e);
            return null; // Consider returning an empty JSONObject or throw a custom exception
        }
    }

    public JSONArray getJsonArrayToApi(String requestURL) {
        try {
            Mono<String> responseMono = webClientBuilder.build()
                    .get()
                    .uri(requestURL)
                    .header("User-Agent", "Mozilla/5.0")
                    .header("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                    .header("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8")
                    .header("Origin", "https://developer.riotgames.com")
                    .retrieve()
                    .bodyToMono(String.class);

            String responseBody = responseMono.block(); // Blocking for simplicity

            JSONParser jParser = new JSONParser();
            Object parsedObject = jParser.parse(responseBody);

            if (parsedObject instanceof JSONArray) {
                return (JSONArray) parsedObject;
            } else {
                log.warn("Received JSON object instead of JSON array");
                return new JSONArray(); // Return an empty JSON array
            }
        } catch (HttpClientErrorException e) {
            if (e.getRawStatusCode() == 404) {
                log.error("Failed to get data: HTTP 404 Not Found");
                return new JSONArray(); // An empty JSONArray object
            } else {
                log.error("response is error: {}", e.getRawStatusCode());
                return new JSONArray();
            }
        } catch (Exception e) {
            log.error("An error occurred while making the HTTP request", e);
            return new JSONArray(); // Consider returning an empty JSONArray or throw a custom exception
        }
    }

    /**
     * getUserInfoByLocalAccount, getUserInfoByRiotAccount 함수들로 끌어온 유저의 puuid로 플레이어의 랭크정보를 받아옴
     *
     * @param name 소환사의 이름
     * @param tag  라이엇 계정일경우 태그를 받아옴, 로컬계정인경우 KR1으로 고정
     * @return 승패 수,
     */
    public JSONObject findUserInfomation(String name, String tag) {
        log.info("접근함");
        log.error("tag : {}", tag);
        //String parseName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        //String parseTag = URLEncoder.encode(tag, StandardCharsets.UTF_8);
        String requestUserInfoUrl = "";
        JSONObject jsonUserInfo = new JSONObject();
        JSONArray leagueJson = new JSONArray();

        if (tag.equals("KR1")) {
            requestUserInfoUrl = riotUrl + "/lol/summoner/v4/summoners/by-name/" + name + "?api_key=" + appKey;
        } else {
            requestUserInfoUrl = aisaRiotUrl + "/riot/account/v1/accounts/by-riot-id/" + name + "/" + tag + "?api_key=" + appKey;
        }
        try {
            if (tag.equals("KR1")) {
                try {
                    log.warn("requestUserInfoUrl : {}", requestUserInfoUrl);
                    jsonUserInfo = getJsonToApi(requestUserInfoUrl);
                    puuid = jsonUserInfo.get("puuid").toString();
                    infoId = jsonUserInfo.get("id").toString();
                    log.error("jsonUserInfo : {}", jsonUserInfo);
                } catch (NullPointerException e) {
                    log.warn("error: {}", e.getMessage());
                }
            } else {
                try {
                    jsonUserInfo = getJsonToApi(requestUserInfoUrl);
                    puuid = jsonUserInfo.get("puuid").toString();
                    try {
                        log.error("puuid: {}", puuid);
                    } catch (Exception e) {
                        log.error("Error : {}", e.getMessage());
                    }

                    String riotAccountUrl = riotUrl + "/lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=" + appKey;
                    JSONObject riotAccountInfo = getJsonToApi(riotAccountUrl);
                    infoId = riotAccountInfo.get("id").toString();
                    for (Object key : jsonUserInfo.keySet()) {
                        riotAccountInfo.remove(key);
                    }
                    jsonUserInfo.putAll(riotAccountInfo);
                } catch (NullPointerException e) {
                    log.warn("error: {}", e.getMessage());
                }
            }
            String leagueInfo = riotUrl + "/lol/league/v4/entries/by-summoner/" + infoId + "?api_key=" + appKey;
            leagueJson = getJsonArrayToApi(leagueInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject result = new JSONObject();
        result.put("userInfo", jsonUserInfo);
        result.put("leagueInfo", leagueJson);
        return result;
    }

    public List<Map<String, Object>> getRecentGames(int start, int count) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        try {
            String recentGamesMatchIdRequestUrl = aisaRiotUrl + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?" +
                    "&start=" + start + "&count=" + count + "&api_key=" + appKey;

            List<String> matchIds = webClientBuilder.build()
                    .get()
                    .uri(recentGamesMatchIdRequestUrl)
                    .header("Accept", "*/*")
                    .retrieve()
                    .onStatus(HttpStatus::isError, clientResponse -> {
                        // 오류 처리, 여기서 더 구체적인 예외를 고려하십시오.
                        return Mono.error(new RuntimeException("경기 ID를 가져오는 중 오류 발생: " + clientResponse.statusCode()));
                    })
                    .bodyToMono(String.class)
                    .<List<String>>handle((response, sink) -> {
                        try {
                            sink.next(mapper.readValue(response, new TypeReference<List<String>>() {
                            }));
                        } catch (JsonProcessingException e) {
                            sink.error(new RuntimeException(e));
                        }
                    })
                    .block(); // 단순화를 위해 차단; 실제 앱에서는 반응적으로 처리

            List<Map<String, Object>> filteredGameData = new ArrayList<>();
            assert matchIds != null;
            for (String matchId : matchIds) {
                String recentGamesUrl = aisaRiotUrl + "/lol/match/v5/matches/" + matchId + "?api_key=" + appKey;

                Map<String, Object> singleGameResult = webClientBuilder.build()
                        .get()
                        .uri(recentGamesUrl)
                        .header("Accept", "*/*")
                        .retrieve()
                        .onStatus(HttpStatus::isError, clientResponse -> {
                            return Mono.error(new RuntimeException("경기 데이터를 가져오는 중 오류 발생: " + clientResponse.statusCode()));
                        })
                        .bodyToMono(String.class)
                        .mapNotNull(response -> {
                            try {
                                return processGameData(response, mapper);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        }) // 보조 메서드로 추출
                        .block();
                filteredGameData.add(singleGameResult);
            }

            return filteredGameData;
        } catch (Exception e) {
            log.error("HTTP 요청 중 오류 발생", e);
            return Collections.emptyList();
        }
    }

    private Map<String, Object> processGameData(String responseBody, ObjectMapper mapper) throws JsonProcessingException {
        Map<String, Object> singleGameResult = new HashMap<>();

        JsonNode root = mapper.readTree(responseBody);
        JsonNode info = root.path("info");
        JsonNode metadata = root.path("metadata");

        int gameDuration = info.path("gameDuration").asInt();
        long gameStartTimestamp = info.path("gameStartTimestamp").asLong();
        long gameEndTimestamp = info.path("gameEndTimestamp").asLong();
        int queueId = info.path("queueId").asInt();
        long gameId = info.path("gameId").asLong();
        String gameMode = info.path("gameMode").asText();

        String matchIdss = String.valueOf(metadata.path("matchId"));

        GameDataResponseDTO gameData = new GameDataResponseDTO();
        gameData.setGameDuration(gameDuration);
        gameData.setGameStartTimestamp((long) gameStartTimestamp);
        gameData.setGameEndTimestamp((long) gameEndTimestamp);
        gameData.setQueueId(queueId);
        gameData.setGameId(gameId);
        gameData.setMatchId(matchIdss);
        gameData.setGameMode(gameMode);

        singleGameResult.put("info", gameData);

        ArrayNode participants1 = (ArrayNode) info.path("participants");
        ArrayNode teams = (ArrayNode) info.path("teams");

        if (participants1.isArray()) {
            List<MatchDataResponseDTO> participants = new ArrayList<>();
            for (JsonNode participantNode : participants1) {
                MatchDataResponseDTO matchDataResponseDTO = mapper.treeToValue(participantNode, MatchDataResponseDTO.class);
                JsonNode perksNode = participantNode.path("perks");
                PerksDataResponseDTO perksDataResponseDTO = mapper.treeToValue(perksNode, PerksDataResponseDTO.class);
                matchDataResponseDTO.setPerks(perksDataResponseDTO);
                participants.add(matchDataResponseDTO);
            }
            singleGameResult.put("participants", participants);
        } else {
            log.warn("participants1.participants is not an array");
        }

        if (teams.isArray()) {
            List<TeamDataResponseDTO> teamsList = new ArrayList<>();
            for (JsonNode teamNode : teams) {
                TeamDataResponseDTO teamDataResponseDTO = mapper.treeToValue(teamNode, TeamDataResponseDTO.class);
                teamsList.add(teamDataResponseDTO);
            }
            singleGameResult.put("teams", teamsList);
        } else {
            log.warn("info.teams is not an array");
        }

        return singleGameResult;
    }

    public List<ChampionMasteryResponseDTO> getChampionMastery() {
        String url = "https://kr.api.riotgames.com/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid + "/top?count=3&api_key=" + appKey;
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        try {
            WebClient.ResponseSpec responseSpec = webClient.get()
                    .retrieve();

            List<ChampionMasteryResponseDTO> championMasteries = responseSpec.bodyToMono(new ParameterizedTypeReference<List<ChampionMasteryResponseDTO>>() {
                    })
                    .block();

            if (championMasteries != null) {
                return championMasteries;
            } else {
                log.error("Error fetching champion mastery data.");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("Server to Server Failed : {}", e.getMessage());
        }
        return null;
    }

    public List<AllChampionMasteryResponseDTO> getAllChampionMastery() {
        String url = riotUrl + "/lol/champion-mastery/v4/champion-masteries/by-puuid/" + puuid + "?api_key=" + appKey;
        log.error("all champion mastery url : {}", url);
        WebClient webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();

        try {
            WebClient.ResponseSpec responseSpec = webClient.get().retrieve();

            List<AllChampionMasteryResponseDTO> championMasteryResponseDTOS = responseSpec.bodyToMono(new ParameterizedTypeReference<List<AllChampionMasteryResponseDTO>>() {
            }).block();

            if (championMasteryResponseDTOS != null) {
                return championMasteryResponseDTOS;
            } else {
                log.error("데이터 가져오는데 실패함ㅋ");
                return Collections.emptyList();
            }
        } catch (Exception e) {
            log.error("통신 실패함 : {}", e.getMessage());
        }
        return null;
    }

    public CurrentGameInfo getRealtimeGame() {
        String url = riotUrl + "/lol/spectator/v4/active-games/by-summoner/" + infoId + "?api_key=" + appKey;
        log.error("url : {}", url);
        WebClient getGame = WebClient.builder()
                .baseUrl(url)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
        Mono<CurrentGameInfo> mono = getGame.get()
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("현재 진행중인 게임이 없습니다.")))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("서버 에러 발생")))
                .bodyToMono(CurrentGameInfo.class);

        CurrentGameInfo realtimeGame = mono.onErrorResume(error -> Mono.empty())
                .block();

        if (realtimeGame == null) {
            return null;
        }

        List<String> puuids = realtimeGame.getParticipants()
                .stream()
                .map(CurrentGameParticipant::getPuuid)
                .collect(Collectors.toList());

        Flux<AllSummoner> allSummonerFlux = Flux.fromIterable(puuids)
                .flatMap(this::getSummonerData);

        List<AllSummoner> allSummoners = allSummonerFlux.collectList().block();

        realtimeGame.setAllSummoners(allSummoners);

        List<CurrentGameParticipant> participants = realtimeGame.getParticipants();

        participants = participants.stream()
                .map(participant -> {
                    assert allSummoners != null;
                    Optional<AllSummoner> summoner = allSummoners.stream()
                            .filter(s -> s.getPuuid().equals(participant.getPuuid()))
                            .findFirst();

                    participant.setGameName(summoner.map(AllSummoner::getGameName).orElse(null));
                    participant.setTagLine(summoner.map(AllSummoner::getTagLine).orElse(null));

                    return participant;
                })
                .collect(Collectors.toList());

        return realtimeGame;
    }

    private Mono<AllSummoner> getSummonerData(String summonerPuuid) {

        String url = aisaRiotUrl + "/riot/account/v1/accounts/by-puuid/" + summonerPuuid + "?api_key=" + appKey;
        // 태그에 대한 조건부 해야함

        return WebClient.create()
                .get()
                .uri(url)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .header("Accept", "*/*")
                .retrieve()
                .bodyToMono(AllSummoner.class);
    }

    public RankingResponseDTO getRankingData(String s) {
        String url = "";
        if (s.equals("CHALLENGER")) {
            url = riotUrl + "/lol/league/v4/challengerleagues/by-queue/RANKED_SOLO_5x5?api_key=" + appKey;
        } else if (s.equals("GRANDMASTER")) {
            url = riotUrl + "/lol/league/v4/grandmasterleagues/by-queue/RANKED_SOLO_5x5?api_key=" + appKey;
        } else if (s.equals("MASTER")) {
            url = riotUrl + "/lol/league/v4/masterleagues/by-queue/RANKED_SOLO_5x5?api_key=" + appKey;
        }

        log.warn("ranking url : {}", url);

        try {
            ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                    .codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(3 * 1024 * 1024))
                    .build();

            // WebClient 객체 생성
            WebClient webClient = WebClient.builder()
                    .exchangeStrategies(exchangeStrategies)
                    .baseUrl(url)
                    .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                    .build();

            // Riot API에서 데이터를 가져옴
            Mono<RankingResponseDTO> mono = webClient.get()
                    .retrieve()
                    .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("연결 오류 발생")))
                    .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("서버 오류 발생")))
                    .bodyToMono(RankingResponseDTO.class);

            // Mono에서 데이터를 가져와 RankingResponseDTO로 변환
            RankingResponseDTO rankingResponseDTO = mono.block();
            log.warn("rankingResponseDTO : {}", rankingResponseDTO);

            if (rankingResponseDTO != null && rankingResponseDTO.getEntries() != null) {
                List<RankingResponseDTO.RankingEntryDTO> entries = rankingResponseDTO.getEntries();
                Collections.sort(entries, Comparator.comparingInt(RankingResponseDTO.RankingEntryDTO::getLeaguePoints).reversed());
                rankingResponseDTO.setEntries(entries);
            }

            return rankingResponseDTO;
        } catch (Exception e) {
            log.error("error : {}", e.getMessage());
            return null;
        }

    }

    //private Mono<SummonerInfo> getSummonerDataBySummonerId(String summonerId) {
    //
    //    String url = riotUrl + "/lol/summoner/v4/summoners/" + summonerId + "?api_key=" + appKey;
    //
    //    return WebClient.create()
    //            .get()
    //            .uri(url)
    //            .header(HttpHeaders.CONTENT_TYPE, "application/json")
    //            .header("Accept", "*/*")
    //            .retrieve()
    //            .bodyToMono(SummonerInfo.class);
    //}
}