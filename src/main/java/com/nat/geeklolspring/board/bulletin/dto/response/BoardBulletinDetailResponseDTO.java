package com.nat.geeklolspring.board.bulletin.dto.response;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardBulletinDetailResponseDTO {


    private Long bulletinId;
    private String title;
    private String posterId;
    private String content;
    private String boardMedia;
    private int viewCount;
    private LocalDateTime localDateTime;

    public BoardBulletinDetailResponseDTO(BoardBulletin boardBulletin) {
        this.bulletinId = boardBulletin.getBulletinId();
        this.posterId = String.valueOf(boardBulletin.getPosterId());
        this.title = boardBulletin.getTitle();
        this.content = boardBulletin.getBoardContent();
        this.boardMedia = boardBulletin.getBoardMedia();
        this.viewCount = boardBulletin.getViewCount();
        this.localDateTime = boardBulletin.getBoardDate();
    }

}
