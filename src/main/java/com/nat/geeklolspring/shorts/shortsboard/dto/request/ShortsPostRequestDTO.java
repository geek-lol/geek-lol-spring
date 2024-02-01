package com.nat.geeklolspring.shorts.shortsboard.dto.request;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardShorts;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortsPostRequestDTO {
    // 동영상 업로드할 때 필요한 정보들
    private String title;
    private String context;
    private MultipartFile videoLink;
    private MultipartFile videoThumbnail;

    public BoardShorts toEntity(String videoLink, String thumbnail, TokenUserInfo userInfo) {
        return BoardShorts.builder()
                .title(title)
                .context(context)
                .videoLink(videoLink)
                .thumbnailImage(thumbnail)
                .uploaderId(userInfo.getUserId())
                .uploaderName(userInfo.getUserName())
                .build();
    }
}
