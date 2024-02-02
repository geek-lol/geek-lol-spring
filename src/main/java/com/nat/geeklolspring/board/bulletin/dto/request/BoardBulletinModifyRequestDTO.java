package com.nat.geeklolspring.board.bulletin.dto.request;

import com.nat.geeklolspring.entity.BoardBulletin;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardBulletinModifyRequestDTO {

    private Long bulletinId;
    private String title;
    private String content;
    private String boardMedia;
    private String posterId;

    public BoardBulletin toEntity(Long bulletinId,String fileUrl) {
        return BoardBulletin.builder()
                .bulletinId(bulletinId)
                .title(title)
                .boardContent(content)
                .boardMedia(fileUrl)
                .build();
    }

}
