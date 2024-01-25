package com.nat.geeklolspring.shorts.dto.response;

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
public class ShortsDetailResponseDTO {
    private Long shortsId;
    private String uploaderId;
    private String title;
    private int viewCount;
    private int upCount;
    private String videoLink;
    private LocalDateTime uploadDate;

    public ShortsDetailResponseDTO(BoardShorts shorts) {
        this.shortsId = shorts.getShortsId();
        this.uploaderId = shorts.getUploaderId();
        this.title = shorts.getTitle();
        this.viewCount = shorts.getViewCount();
        this.upCount = shorts.getUpCount();
        this.videoLink = shorts.getVideoLink();
        this.uploadDate = shorts.getUploadDate();
    }
}
