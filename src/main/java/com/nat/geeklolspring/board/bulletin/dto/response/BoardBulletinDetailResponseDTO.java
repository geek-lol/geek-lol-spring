package com.nat.geeklolspring.board.bulletin.dto.response;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.User;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardBulletinDetailResponseDTO {


    private long bulletinId;
    private String title;
    private String posterId;
    private String content;
    private String boardMedia;

    public BoardBulletinDetailResponseDTO(BoardBulletin boardBulletin) {
        this.bulletinId = boardBulletin.getBulletinId();
        this.posterId = String.valueOf(boardBulletin.getPosterId());
        this.title = boardBulletin.getTitle();
        this.content = boardBulletin.getBoardContent();
        this.boardMedia = boardBulletin.getBoardMedia();
    }

}
