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
    private String posterName;
    private LocalDateTime localDateTime;
    private int upCount;
    private int totlaReply; // 총 댓글 수

    public BoardBulletinDetailResponseDTO(BoardBulletin boardBulletin) {
        this.bulletinId = boardBulletin.getBulletinId();
        this.posterId = (boardBulletin.getUser().getId());
        this.posterName = boardBulletin.getUser().getUserName();
        this.title = boardBulletin.getTitle();
        this.content = boardBulletin.getBoardContent();
        this.boardMedia = boardBulletin.getBoardMedia();
        this.viewCount = boardBulletin.getViewCount();
        this.localDateTime = boardBulletin.getBoardDate();
        this.upCount = boardBulletin.getUpCount();
        this.totlaReply = boardBulletin.getReplies().size();
    }

}
