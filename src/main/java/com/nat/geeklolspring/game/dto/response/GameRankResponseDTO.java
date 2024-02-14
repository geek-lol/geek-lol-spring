package com.nat.geeklolspring.game.dto.response;

import com.nat.geeklolspring.entity.CsGameRank;
import com.nat.geeklolspring.entity.ResGameRank;
import com.nat.geeklolspring.entity.User;
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
        this.userId = rank.getUser().getId();
        this.userName = rank.getUser().getUserName();
        this.score = rank.getScore();
        recordDate = rank.getRecordDate();
    }
    public GameRankResponseDTO(CsGameRank rank){
        this.gameId = rank.getGameId();
        this.userId = rank.getUser().getId();
        this.userName = rank.getUser().getUserName();
        this.score = rank.getScore();
        recordDate = rank.getRecordDate();
    }
    public ResGameRank resToEntity(User user){
        return ResGameRank.builder()
                .gameId(gameId)
                .user(user)
                .score(score)
                .recordDate(recordDate)
                .build();
    }
    public CsGameRank csToEntity(User user){
        return CsGameRank.builder()
                .gameId(gameId)
                .user(user)
                .score(score)
                .recordDate(recordDate)
                .build();
    }
}
