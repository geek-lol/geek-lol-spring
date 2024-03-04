# KDT 팀 프로젝트 3조 <br> - NOT (Noah's Ark or Titanic:노아의 방주 또는 타이타닉)
- 사이트 가기 : [GEEK LOL](http://geeklol.site)
- ppt 및 프로젝트 소개 : [canva 바로가기](https://www.canva.com/design/DAF83EX-8eY/HgqQhkd8n76Jt6HcLElocw/view?utm_content=DAF83EX-8eY&utm_campaign=designshare&utm_medium=link&utm_source=editor)
- 개발기간 : 2024-01-18~2024-02-18 (30일)
- 롤 전적검색 & 커뮤니티 사이트입니다.
   - 회원가입을 하여 사이트를 이용해보세요!
   - riot API를 이용하여 유저의 정보를 검색, 상위 랭킹 목록을 조회할 수 있습니다.
   - 상대와의 충돌이 있을 때 해결이 안 된다면 사이트에 올려주세요! 트롤투표소를 통해 유저들의 생각을 들을 수 있습니다
   - 자랑하고싶거나 공유하고싶은 하이라이트 영상을 올리면 쇼츠처럼 다른사람에게 보여줄 수 있습니다.
   - 심심할 때, 반응속도와 미니게임을 할 수 있습니다.

<hr>

## 목차 
1. [팀원 소개](#팀원-소개) <br>
2. [DB 설계도](#db-설계도) <br>
3. [엔드포인트](#사용-엔드포인트) <br>

<hr>

## 팀원 소개 

<center>
  


|    fullStack   |   fullStack   |   FrontEnd   |   FrontEnd   |   backEnd   |   backEnd   |
|:--------------:|:----------------:|:----------------:|:----------------:|:----------------:|:----------------:|
| <a href="https://github.com/yongseopK"> <img src="https://avatars.githubusercontent.com/u/114211344?v=4" width="200"/> </a> | <a href="https://github.com/Limseonjin"> <img src="https://avatars.githubusercontent.com/u/128454779?v=4" width="200"/> </a> |<a href="https://github.com/Ajeabal"> <img src="https://avatars.githubusercontent.com/u/97341527?v=4" width="200"/> </a>| <a href="https://github.com/YOOGARYUNG"> <img src="https://avatars.githubusercontent.com/u/138635792?v=4" width="200"/> </a>| <a href="https://github.com/JuHo99"> <img src="https://avatars.githubusercontent.com/u/138633367?v=4" width="200"/> </a>| <a href="https://github.com/smg0218"> <img src="https://avatars.githubusercontent.com/u/128454773?v=4" width="200"/> </a>|
| [김용섭](https://github.com/yongseopK) | [임선진](https://github.com/Ajeabal) | [이효원](https://github.com/Ajeabal) | [유가령](https://github.com/YOOGARYUNG) | [박주호](https://github.com/JuHo99) | [송민건](https://github.com/smg0218) |

</center>

<hr>

## DB 설계도 
<center>
  <img src="https://github.com/geek-lol/geek-lol-spring/blob/master/READMEIMG/dbERD.png?raw=true" width="800">
</center>

## 사용 엔드포인트 

&nbsp;&nbsp;[ - riot api](#전적검색riot-api-endpoint) <br>
&nbsp;&nbsp;[ - User](#user-endpoint) <br>
&nbsp;&nbsp;[ - 자유게시판](#자유게시판-endpoint) <br>
&nbsp;&nbsp;[ - 쇼츠게시판](#쇼츠게시판-endpoint) <br>
&nbsp;&nbsp;[ - 투표지원게시판](#투표지원게시판-endpoint) <br>
&nbsp;&nbsp;[ - 투표게시판](#투표게시판-endpoint) <br>
&nbsp;&nbsp;[ - 관리자페이지](#관리자페이지-endpoint) <br>
&nbsp;&nbsp;[ - 게임랭킹](#게임랭킹-endpoint) <br>

### 전적검색(riot API) EndPoint
| 기능                     | URL                               | Method |
| ------------------------ | --------------------------------- | ------ |
| 유저정보 조회            | /find/{name}/{tag}                | GET    |
| 최근 플레이 기록         | /recentGames/{start}/{count}      | GET    |
| 챔피언 숙련도 상위 3개    | /champion/Mastery                 | GET    |
| 모든 챔피언 숙련도        | /all-champion-mastery             | GET    |
| 플레이 중인 인게임 정보   | /realtimeGame                     | GET    |
| 랭킹 조회                | /ranking/{tier}                   | GET    |


### USER EndPoint
| 기능                               | URL                     | Method |
| ---------------------------------- | ----------------------- | ------ |
| 로그인한 회원 조회                  | /user                   | GET    |
| 회원가입 요청                       | /user/sign_up           | POST   |
| 회원가입시 아이디 비동기 체크       | /user/check             | GET    |
| 회원가입시 비밀번호 비동기 체크     | /user/pwcheck           | GET    |
| 로그인                              | /user/signin            | POST   |
| 회원 삭제                           | /user/delete            | POST   |
| 회원 수정                           | /user/modify            | PUT    |
| 로그인한 회원의 프로필 사진불러오기 | /user/load-profile      | GET    |
| 특정회원의 프로필 사진불러오기      | /user/profile/{userId} | GET    |


### 자유게시판 EndPoint

| 기능                    | URL                             | Method |
| ----------------------- | ------------------------------- | ------ |
| 게시물 전체 조회(+검색)        | /board/bullentin                | GET    |
| 특정 게시물 조회         | /board/bullentin/detail         | GET    |
| 내가 쓴 게시물 조회      | /board/bullentin/my             | GET    |
| 게시물의 사진 조회      | /board/bullentin/load-profile   | GET    |
| 게시글 등록             | /board/bullentin                | POST   |
| 게시글 수정             | /board/bullentin/modify         | POST   |
| 게시글 삭제             | /board/bullentin                | DELETE |
||||
| 특정 게시물의 댓글을 조회     | /board/bulletin/detail/reply/{bullentinId} | GET    |
| 내가 쓴 댓글 조회             | /board/bulletin/detail/reply/my            | GET    |
| 특정 게시물에 댓글 등록       | /board/bulletin/detail/reply/{bullentinId} | POST   |
| 댓글을 삭제                   | /board/bulletin/detail/reply/{replyId}     | DELETE |
||||
| 특정 게시물의 좋아요 정보 조회   | /board/vote    | GET          |
| 특정 게시물에 좋아요 생성        | /board/vote    | POST         |
| 특정 게시물에 내 좋아요 수정    | /board/vote    | PUT, PATCH   |

### 쇼츠게시판 EndPoint
| 기능                       | URL                                    | Method |
| -------------------------- | -------------------------------------- | ------ |
| 게시물 전체 조회                  | /api/shorts                             | GET    |
| 내가 쓴 게시물 조회        | /api/shorts/my                          | GET    |
| 동영상 파일 불러오기       | /api/shorts/load-video/{shortsId}      | GET    |
| 게시글 등록               | /api/shorts                             | POST   |
| 게시글 삭제               | /api/shorts                             | DELETE |
||||
| 특정 게시물의 댓글을 조회     | /api/shorts/reply/{shortsId}          | GET    |
| 내가 쓴 댓글 조회             | /api/shorts/reply/my                     | GET    |
| 특정 게시물에 댓글 등록       | /api/shorts/reply/{shortsId}          | POST   |
| 댓글을 삭제                   | /api/shorts/reply/{replyId}              | DELETE |
||||
| 특정 게시물의 좋아요 정보 조회   |  /api/vote     | GET          |
| 특정 게시물에 좋아요 생성        |  /api/vote     | POST         |
| 특정 게시물에 내 좋아요 수정    |  /api/vote     | PUT, PATCH   |

### 투표지원게시판 EndPoint
| 기능                       | URL                                      | Method |
| -------------------------- | ---------------------------------------- | ------ |
| 게시물 전체 조회           | /troll/apply                             | GET    |
| 특정 게시물 조회           | /troll/detail/{applyId}                  | GET    |
| 내가 쓴 게시물 조회        | /troll/apply/my                          | GET    |
| 게시물 검색하기            | /troll/apply/search                      | GET    |
| 동영상 파일 불러오기       | /troll/apply/load-video/{applyId}      | GET    |
| 게시글 등록               | /troll/apply                             | POST   |
| 게시글 삭제               | /troll/apply                             | DELETE |
| 투표 갱신일 조회          | /troll/apply/endTime                     | GET    |
||||
| 특정 게시물의 댓글을 조회     | /troll/apply/reply/{applyId}            | GET          |
| 내가 쓴 댓글 조회             | /troll/apply/reply/my                    | GET          |
| 특정 게시물에 댓글 등록       | /troll/apply/reply/{applyId}             | POST         |
| 댓글을 삭제                   | /troll/apply/reply/{replyId}              | DELETE       |
||||
| 특정 게시물의 좋아요 정보 조회   | /troll/apply/vote                        | GET          |
| 특정 게시물에 좋아요 생성        | /troll/apply/vote                        | POST         |
| 특정 게시물에 내 좋아요 수정    | /troll/apply/vote                        | PUT, PATCH   |

### 투표게시판 EndPoint
| 기능                     | URL                                              | Method |
| ------------------------ | ------------------------------------------------ | ------ |
| 최근 게시물 2개 조회      | /troll/ruling/board                             | GET    |
| 전체 게시물 조회          | /troll/ruling/board/all                         | GET    |
| 게시물 상세 조회          | /troll/ruling/board/{rulingId}                  | GET    |
| 내 게시물 조회            | /troll/ruling/board/my                          | GET    |
||||
| 특정 게시물의 댓글을 조회     | /troll/ruling/reply/{rulingId}            | GET   |
| 내가 쓴 댓글 조회             | /troll/ruling/reply/my                    | GET    |
| 특정 게시물에 댓글 등록       | /troll/ruling/reply/{rulingId}            | POST  |
| 댓글을 삭제                   | /troll/ruling/reply/{replyId}             | DELETE  |
||||
| 특정 게시물의 파일 불러오기 | /troll/ruling/board/load-video/{rulingId}      | GET    |
| 특정 게시물의 투표정보 조회 | /troll/ruling/vote                              | GET    |
| 특정 게시물에 투표내역 저장 | /troll/ruling/vote                              | POST   |

### 관리자페이지 EndPoint
| 기능                           | URL                 | Method |
| ------------------------------ | ------------------- | ------ |
| 전체 유저 조회                | /admin/user         | POST   |
| 유저 삭제 (List)               | /admin/user         | DELETE |
| 유저 권한 수정                | /admin/change       | POST   |
| 전체 자유게시판 조회           | /admin/board        | POST   |
| 선택한 자유게시물 삭제 (List)   | /admin/board        | DELETE |
| 전체 쇼츠게시판 조회           | /admin/shorts       | POST   |
| 선택한 쇼츠게시물 삭제 (List)   | /admin/shorts       | DELETE |
| 전체 투표게시판 조회           | /admin/ruling       | POST   |
| 선택한 투표게시물 삭제 (List)   | /admin/ruling       | DELETE |
| 전체 투표지원게시판 조회       | /admin/applys       | POST   |
| 선택한 투표지원게시물 삭제 (List) | /admin/applys     | DELETE |

### 게임랭킹 EndPoint
| 기능          | URL         | Method |
| ------------- | ----------- | ------ |
| 막타게임 랭킹 조회     | /game/cs    | GET    |
| 막타게임 랭킹 등록     | /game/cs    | POST   |
| 반응속도게임 랭킹 조회     | /game/res   | GET    |
| 반응속도게임 랭킹 등록     | /game/res   | POST   |
