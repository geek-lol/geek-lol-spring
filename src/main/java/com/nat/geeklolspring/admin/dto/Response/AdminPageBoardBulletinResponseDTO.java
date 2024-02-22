package com.nat.geeklolspring.admin.dto.Response;

import com.nat.geeklolspring.entity.BoardBulletin;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPageBoardBulletinResponseDTO {
    private Long bulletinId;
    private String title;
    private int viewCount;
    private String posterName;
    private LocalDateTime localDateTime;
    private int upCount;

    public AdminPageBoardBulletinResponseDTO (BoardBulletin boardBulletin){
        this.bulletinId = boardBulletin.getBulletinId();
        this.posterName = boardBulletin.getUser().getUserName();
        this.title = boardBulletin.getTitle();
        this.viewCount = boardBulletin.getViewCount();
        this.localDateTime = boardBulletin.getBoardDate();
        this.upCount = boardBulletin.getUpCount();
    }

}
