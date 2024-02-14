package com.nat.geeklolspring.board.bulletin.dto.response;

import com.nat.geeklolspring.entity.BoardBulletin;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardBulletinDeleteResponseDTO {
    private Long bulletinId;
    private String title;
    private String posterId;
    private List<Long> ids;

    public BoardBulletinDeleteResponseDTO(BoardBulletin boardBulletin) {
        this.bulletinId = boardBulletin.getBulletinId();
        this.posterId = String.valueOf(boardBulletin.getPosterId());
        this.title = boardBulletin.getTitle();
    }

}
