package com.nat.geeklolspring.lolapi.services;

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
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class LolApiService {

    @Value("${lol.app-key}")
    private String appKey;

    private String riotUrl = "https://kr.api.riotgames.com";
    private String aisaRiotUrl = "https://asia.api.riotgames.com";

    String puuid = "";


    /**
     * 라이엇 계정말고, 리그오브레전드 아이디로 요청을 보낼 시 해당 유저의 정보들을 뱉어줌
     * @param requestURL api 호출 주소
     * @return api로 끌고온 데이터
     * @throws Exception 아마 정보가 없을때?
     */
    public JSONObject getJsonToApi(String requestURL) throws Exception {
        JSONObject jsonObj = new JSONObject();
        JSONParser jParser = new JSONParser();

        HttpGet httpGet = new HttpGet(requestURL);

        httpGet.addHeader("User-Agent", "Mozilla/5.0");
        httpGet.addHeader("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        httpGet.addHeader("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
        httpGet.addHeader("Origin", "https://developer.riotgames.com");
        //httpGet.addHeader("X-Riot-Token", appKey);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = httpClient.execute(httpGet);

        if(response.getStatusLine().getStatusCode() == 200) {
            ResponseHandler<String> handler = new BasicResponseHandler();
            String body = handler.handleResponse(response);
            log.info("body : " + body);

            jsonObj = (JSONObject) jParser.parse(body);
        } else {
            log.error("response is error : " + response.getStatusLine().getStatusCode());
        }

        return jsonObj;
    }

    /**
     * 유저의 리그(랭크)정보를 가져오는 함수임
     *
     * @param requestURL api 호출 주소
     * @return 리그(랭크) 정보
     * @throws Exception 끌어올 정보가 없을때
     */
    public JSONArray getJsonArrayToApi(String requestURL) throws Exception {
        JSONArray jsonArray = new JSONArray();
        JSONParser jParser = new JSONParser();

        HttpGet httpGet = new HttpGet(requestURL);

        httpGet.addHeader("User-Agent", "Mozilla/5.0");
        httpGet.addHeader("Accept-Language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7");
        httpGet.addHeader("Accept-Charset", "application/x-www-form-urlencoded; charset=UTF-8");
        httpGet.addHeader("Origin", "https://developer.riotgames.com");
        // httpGet.addHeader("X-Riot-Token", appKey);

        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = httpClient.execute(httpGet);

        if (response.getStatusLine().getStatusCode() == 200) {
            ResponseHandler<String> handler = new BasicResponseHandler();
            String body = handler.handleResponse(response);
            log.info("body: " + body);

            Object parsedObject = jParser.parse(body);

            if (parsedObject instanceof JSONArray) {
                jsonArray = (JSONArray) parsedObject;
            } else {
                log.warn("Received JSON object instead of JSON array");
            }
        } else {
            log.error("response is error: " + response.getStatusLine().getStatusCode());
        }
        return jsonArray;
    }


    /**
     * getUserInfoByLocalAccount, getUserInfoByRiotAccount 함수들로 끌어온 유저의 puuid로 플레이어의 랭크정보를 받아옴
     * @param name 소환사의 이름
     * @param tag 라이엇 계정일경우 태그를 받아옴, 로컬계정인경우 KR1으로 고정
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


        if(tag.equals("KR1")) {
            requestUserInfoUrl = riotUrl + "/lol/summoner/v4/summoners/by-name/" + parseName + "?api_key=" + appKey;
        } else {
            requestUserInfoUrl = aisaRiotUrl + "/riot/account/v1/accounts/by-riot-id/" + parseName + "/" + parseTag + "?api_key=" + appKey;
        }

        log.warn("requestURL : {}", requestUserInfoUrl);
        try {
            if (tag.equals("KR1")) {
                jsonUserInfo = getJsonToApi(requestUserInfoUrl);
                puuid = jsonUserInfo.get("puuid").toString();
                infoId = jsonUserInfo.get("id").toString();
                log.info("puuid : {}", puuid);
            } else {
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


        log.error("result : {}", result);
        return result;
    }

    public JSONArray getRecentGames(int start, int count) {
        String recentGameRequestUrl = aisaRiotUrl + "/lol/match/v5/matches/by-puuid/" + puuid + "/ids?start=1&count=20&api_key=" + appKey;
        log.info("recentGameRequestUrl : {}", recentGameRequestUrl);
        JSONArray jsonToApi = new JSONArray();

        try {
            jsonToApi = getJsonArrayToApi(recentGameRequestUrl);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonToApi;
    }

}


