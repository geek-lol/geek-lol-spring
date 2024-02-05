package com.nat.geeklolspring.game.dto.response;

import com.nat.geeklolspring.entity.ResGameRank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameRankResponseDTO {
    private Long gameId;
    private String userId;
    private String userName;
    private float score;
    private LocalDateTime recordDate;

    public GameRankResponseDTO(ResGameRank rank){
        this.gameId = rank.getGameId();
        this.userId = rank.getUserId();
        this.userName = rank.getUserName();
        this.score = rank.getScore();
        recordDate = rank.getRecordDate();
    }
    public ResGameRank toEntity(){
        return ResGameRank.builder()
                .gameId(gameId)
                .userId(userId)
                .score(score)
                .userName(userName)
                .recordDate(recordDate)
                .build();
    }
}
