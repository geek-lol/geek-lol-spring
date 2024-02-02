package com.nat.geeklolspring.lolapi.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nat.geeklolspring.lolapi.dto.response.GameDataResponseDTO;
import com.nat.geeklolspring.lolapi.dto.response.PerksDataResponseDTO;
import com.nat.geeklolspring.lolapi.dto.response.TeamDataResponseDTO;
import com.nat.geeklolspring.lolapi.dto.response.MatchDataResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
public class LolApiService {
    @Value("${lol.app-key}")
    private String appKey;
    private String riotUrl = "https://kr.api.riotgames.com";
    private String aisaRiotUrl = "https://asia.api.riotgames.com";
    String puuid = "";

    public JSONObject getJsonToApi(String requestURL) {
        JSONObject jsonObj = new JSONObject();
        JSONParser jParser = new JSONParser();
        HttpGet httpGet = new HttpGet(requestURL);

        httpGet.addHeader("User-Agent", "Mozilla/5.0");
        httpGet.addHeader("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        httpGet.addHeader("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
        httpGet.addHeader("Origin", "https://developer.riotgames.com");
        // httpGet.addHeader("X-Riot-Token", appKey);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    ResponseHandler<String> handler = new BasicResponseHandler();
                    String body = handler.handleResponse(response);
                    //log.info("body : " + body);
                    jsonObj = (JSONObject) jParser.parse(body);
                } else if (statusCode == 404) {
                    log.error("Failed to get data: HTTP 404 Not Found");
                    // Returns null
                    return null;
                } else {
                    log.error("response is error : " + statusCode);
                }
            } catch (Exception e) {
                log.error("An error occurred while making the HTTP request", e);
            }
        } catch (Exception e) {
            log.error("An error occurred while creating the HTTP client", e);
        }
        if (jsonObj == null){
            log.warn("The JSON object 'jsonObj' is null");
            jsonObj = new JSONObject();
        }
        return jsonObj;
    }

    public JSONArray getJsonArrayToApi(String requestURL) {
        JSONArray jsonArray = new JSONArray();
        JSONParser jParser = new JSONParser();
        HttpGet httpGet = new HttpGet(requestURL);

        httpGet.addHeader("User-Agent", "Mozilla/5.0");
        httpGet.addHeader("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        httpGet.addHeader("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
        httpGet.addHeader("Origin", "https://developer.riotgames.com");
        // httpGet.addHeader("X-Riot-Token", appKey);

        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    ResponseHandler<String> handler = new BasicResponseHandler();
                    String body = handler.handleResponse(response);
                    //log.info("body: " + body);
                    Object parsedObject = jParser.parse(body);

                    if (parsedObject instanceof JSONArray) {
                        jsonArray = (JSONArray) parsedObject;
                    } else {
                        log.warn("Received JSON object instead of JSON array");
                    }
                } else if (statusCode == 404) {
                    log.error("Failed to get data: HTTP 404 Not Found");
                    // An empty JSONArray object
                    return new JSONArray();
                } else {
                    log.error("response is error: " + statusCode);
                }
            } catch (Exception e) {
                log.error("An error occurred while making the HTTP request", e);
            }
        } catch (Exception e) {
            log.error("An error occurred while creating the HTTP client", e);
        }

        if(jsonArray == null) {
            jsonArray = new JSONArray();
        }
        return jsonArray;
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
        String parseName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        String parseTag = URLEncoder.encode(tag, StandardCharsets.UTF_8);
        String requestUserInfoUrl = "";
        String infoId = "";
        JSONObject jsonUserInfo = new JSONObject();
        JSONArray leagueJson = new JSONArray();

        if (tag.equals("KR1")) {
            requestUserInfoUrl = riotUrl + "/lol/summoner/v4/summoners/by-name/" + parseName + "?api_key=" + appKey;
        } else {
            requestUserInfoUrl = aisaRiotUrl + "/riot/account/v1/accounts/by-riot-id/" + parseName + "/" + parseTag + "?api_key=" + appKey;
        }
        log.warn("requestURL : {}", requestUserInfoUrl);
        try {
            if (tag.equals("KR1")) {
                try{
                    jsonUserInfo = getJsonToApi(requestUserInfoUrl);
                    puuid = jsonUserInfo.get("puuid").toString();
                    infoId = jsonUserInfo.get("id").toString();
                    log.info("puuid : {}", puuid);
                } catch (NullPointerException e) {
                    log.warn("error: {}", e.getMessage());
                }
            } else {
                try {
                    log.warn("이건 라이엇계정임");
                    jsonUserInfo = getJsonToApi(requestUserInfoUrl);
                    puuid = jsonUserInfo.get("puuid").toString();
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
            log.warn(leagueInfo);
            leagueJson = getJsonArrayToApi(leagueInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject result = new JSONObject();
        result.put("userInfo", jsonUserInfo);
        result.put("leagueInfo", leagueJson);

        //log.error("result : {}", result);
        return result;
    }

    public List<Map<String, Object>> getRecentGames(int start, int count) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        RestTemplate restTemplate = new RestTemplate();

        try {
            //String recentGamesMatchIdRequestUrl = aisaRiotUrl + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?queue=420&start=" + 0 + "&count=" + 10 + "&api_key=" + appKey;
            String recentGamesMatchIdRequestUrl = aisaRiotUrl + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=" + 0 + "&count=" + 10 + "&api_key=" + appKey;
            log.info("recentGamesMatchIdRequestUrl : {}", recentGamesMatchIdRequestUrl);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Accept","*/*");


            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(recentGamesMatchIdRequestUrl, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                List<String> matchIds = mapper.readValue(response.getBody(), new TypeReference<>() {
                });

                //Map<String, Object> filteredGameData = new HashMap<>();
                List<Map<String, Object>> filteredGameData = new ArrayList<>();

                for (String matchId : matchIds) {
                    String recentGamesUrl = aisaRiotUrl + "/lol/match/v5/matches/" + matchId + "?api_key=" + appKey;
                    log.warn("recentGamesUrl : {}", recentGamesUrl);

                    ResponseEntity<String>  Aresponse = restTemplate.exchange(recentGamesUrl,HttpMethod.GET, entity, String.class);
                    log.warn("response body : {}", Aresponse.getStatusCode());

                    Map<String, Object> singleGameResult = new HashMap<>();
                    if (Aresponse.getStatusCode().is2xxSuccessful()) {
                        JsonNode root = mapper.readTree(Aresponse.getBody());
                        JsonNode info = root.path("info");
                        JsonNode metadata = root.path("metadata");

                        int gameDuration = info.path("gameDuration").asInt();
                        long gameStartTimestamp = info.path("gameStartTimestamp").asLong();
                        long gameEndTinestamp = info.path("gameEndTimestamp").asLong();
                        int queueId = info.path("queueId").asInt();
                        long gameId = info.path("gameId").asLong();
                        String gameMode = info.path("gameMode").asText();

                        String matchIdss = String.valueOf(metadata.path("matchId"));


                        GameDataResponseDTO gameData = new GameDataResponseDTO();
                        gameData.setGameDuration(gameDuration);
                        gameData.setGameStartTimestamp((long) gameStartTimestamp);
                        gameData.setGameEndTimestamp((long) gameEndTinestamp);
                        gameData.setQueueId(queueId);
                        gameData.setGameId(gameId);
                        gameData.setMatchId(matchIdss);
                        gameData.setGameMode(gameMode);


                        singleGameResult.put("info", gameData);



                        ArrayNode participants1 = (ArrayNode) info.path("participants");
                        ArrayNode teams = (ArrayNode) info.path("teams");

                        if (participants1.isArray()) {
                            //List<String> participants = mapper.convertValue(participants1, new TypeReference<List<String>>() {});
                            List<MatchDataResponseDTO> participants = new ArrayList<>();
                            for (JsonNode participantNode : participants1) {
                                MatchDataResponseDTO matchDataResponseDTO = mapper.treeToValue(participantNode, MatchDataResponseDTO.class);

                                JsonNode perksNode = participantNode.path("perks");
                                PerksDataResponseDTO perksDataResponseDTO = mapper.treeToValue(perksNode, PerksDataResponseDTO.class);


                                matchDataResponseDTO.setPerks(perksDataResponseDTO);


                                participants.add(matchDataResponseDTO);
                            }
                            //log.error("participants : {}", participants);
                            //filteredGameData.put("participants", participants);
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

                        //filteredGameData.put("info", mapper.convertValue(info, new TypeReference<Map<String, Object>>() {
                        //}));
                        //filteredGameData.put("participants1", mapper.convertValue(participants1, new TypeReference<Map<String, Object>>() {
                        //}));
                        //log.warn("participants1 : {}", participants1);

                        //log.warn("filteredGameData : {}", filteredGameData);
                        filteredGameData.add(singleGameResult);
                    } else {
                        log.error("Error fetching recent games data. Status code: {}", Aresponse.getStatusCodeValue());
                    }
                }

                return filteredGameData;
            } else {
                log.error("Error fetching recent games match IDs. Status code: {}", response.getStatusCodeValue());
            }
        } catch (HttpClientErrorException e) {
            // Handle 4xx client errors
            log.error("Client error while making HTTP request: {}", e.getRawStatusCode());
            log.error("Response body: {}", e.getResponseBodyAsString());
        } catch (HttpServerErrorException e) {
            // Handle 5xx server errors
            log.error("Server error while making HTTP request: {}", e.getRawStatusCode());
            log.error("Response body: {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            // Handle other exceptions
            log.error("Exception while making HTTP request", e);
        }

        return Collections.emptyList();
    }

}