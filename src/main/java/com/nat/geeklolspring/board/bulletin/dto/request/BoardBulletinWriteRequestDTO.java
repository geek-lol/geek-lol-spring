package com.nat.geeklolspring.board.bulletin.dto.request;

import com.nat.geeklolspring.entity.BoardBulletin;
import com.nat.geeklolspring.entity.BoardReply;
import com.nat.geeklolspring.entity.User;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<BoardReply> replies = new ArrayList<>();

    public BoardBulletin toEntity(String fileUrl,User user) {
        return BoardBulletin.builder()
                .title(title)
                .boardContent(content)
                .boardMedia(fileUrl)
                .user(user)
                .Replies(replies)
                .build();
    }


}
