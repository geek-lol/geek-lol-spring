package com.nat.geeklolspring.board.vote.dto.request;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BulletinCheck;
import com.nat.geeklolspring.entity.User;
import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardVotePostRequestDTO {
    // 좋아요를 만들 때 필요한 정보들
    @NotNull
    private Long boardId;

    public BulletinCheck toEntity(BoardBulletin bulletin, User user) {
        return BulletinCheck.builder()
                .bulletin(bulletin)
                .user(user)
                .build();
    }
}
