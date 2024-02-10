package com.nat.geeklolspring.shorts.shortsboard.dto.response;

import com.nat.geeklolspring.entity.BoardShorts;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortsMyPageResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private Long shortsId;
    private String uploaderId;
    private String posterName;
    private String title;
    private int viewCount;
    private int upCount;
    private LocalDateTime localDateTime;

    public ShortsMyPageResponseDTO(BoardShorts shorts) {
        this.shortsId = shorts.getShortsId();
        this.uploaderId = shorts.getUploaderId();
        this.title = shorts.getTitle();
        this.viewCount = shorts.getViewCount();
        this.upCount = shorts.getUpCount();
    }
}
