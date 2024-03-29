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
public class ShortsDetailResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private Long shortsId;
    private String uploaderId;
    private String uploaderName;
    private String title;
    private String context;
    private int replyCount;
    private int viewCount;
    private int upCount;
    private String videoLink;
    private LocalDateTime uploadDate;

    public ShortsDetailResponseDTO(BoardShorts shorts) {
        this.shortsId = shorts.getShortsId();
        this.uploaderId = shorts.getUploaderId().getId();
        this.uploaderName = shorts.getUploaderId().getUserName();
        this.title = shorts.getTitle();
        this.context = shorts.getContext();
        this.viewCount = shorts.getViewCount();
        this.upCount = shorts.getUpCount();
        this.replyCount = shorts.getReplyCount();
        this.videoLink = shorts.getVideoLink();
        this.uploadDate = shorts.getUploadDate();
    }
}
