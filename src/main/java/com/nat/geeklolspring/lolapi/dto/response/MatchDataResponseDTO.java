package com.nat.geeklolspring.lolapi.dto.response;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchDataResponseDTO {
    private int                     teamId;                             // 팀 아이디 (ex: 100, 200)
    private String                  summonerId;                         // 롤 계정명
    private String                  riotIdGameName;                     // 라이엇 계정명
    private String                  riotIdTagline;                      // 라이엇 게임태그
    private int                     summoner1Id;                        // 스펠 1
    private int                     summoner2Id;                        // 스펠 2
    private String                  teamPosition;                       // 라인
    private int                     championId;                         // 챔피언 코드
    private int                     kills;                              // 플레이어 킬
    private int                     deaths;                             // 플레이어 데스
    private int                     assists;                            // 플레이어 어시스트
    private int                     neutralMinionsKilled;               // 정글몬스터 처치횟수
    private int                     totalMinionsKilled;                 // 전체 미니언 처치횟수
    private int                     totalDamageDealtToChampions;        // 적 챔피언에게 넣은 데미지

    private int                     wardsKilled;                        // 와드 제거
    private int                     wardsPlaced;                        // 와드 설치

    private String                  championName;                       // 챔피언 이름

    private int                     doubleKills;                        // 더블킬 횟수
    private int                     tripleKills;                        // 트리플킬 횟수
    private int                     quadraKills;                        // 쿼드라킬 횟수
    private int                     pentaKills;                         // 펜타킬 횟수


    private int                     item0;                              // 1번째 아이템
    private int                     item1;                              // 2번째 아이템
    private int                     item2;                              // 3번째 아이템
    private int                     item3;                              // 4번째 아이템
    private int                     item4;                              // .
    private int                     item5;                              // .
    private int                     item6;                              // .

    private PerksDataResponseDTO    perks;                              // 룬 배열

    private String                  allInPings;                         // 뭔지는 모르겠는데 없으면 에러남

    private boolean                 win;                                // 졌는지 이겼는지에 대한 논리값

}
