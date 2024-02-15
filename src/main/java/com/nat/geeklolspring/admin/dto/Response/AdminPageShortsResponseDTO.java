package com.nat.geeklolspring.admin.dto.Response;

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
public class AdminPageShortsResponseDTO {
    private Long shortsId;
    private String uploaderName;
    private String title;
    private int viewCount;
    private LocalDateTime uploadDate;
    private int upCount;

    public AdminPageShortsResponseDTO(BoardShorts shorts) {
        this.shortsId = shorts.getShortsId();
        this.uploaderName = shorts.getUploaderId().getUserName();
        this.title = shorts.getTitle();
        this.viewCount = shorts.getViewCount();
        this.uploadDate = shorts.getUploadDate();
        this.upCount = shorts.getUpCount();
    }
}
