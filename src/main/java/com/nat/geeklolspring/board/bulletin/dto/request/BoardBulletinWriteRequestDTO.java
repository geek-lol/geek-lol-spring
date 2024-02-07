package com.nat.geeklolspring.board.bulletin.dto.request;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardBulletinWriteRequestDTO {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private String boardMedia;
    private String posterId;
    private String posterName;
    @Builder.Default
    private LocalDateTime boardDate = LocalDateTime.now();
    @Builder.Default
    private int boardReportCount = 0;

    public BoardBulletin toEntity(String fileUrl) {
        return BoardBulletin.builder()
                .title(title)
                .boardContent(content)
                .posterId(posterId)
                .posterName(posterName)
                .boardMedia(fileUrl)
                .boardReportCount(boardReportCount)
                .build();
    }


}
