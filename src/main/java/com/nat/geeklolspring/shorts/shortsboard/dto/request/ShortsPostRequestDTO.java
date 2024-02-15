package com.nat.geeklolspring.shorts.shortsboard.dto.request;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.entity.User;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

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

    public BoardShorts toEntity(String videoLink, User user) {
        return BoardShorts.builder()
                .title(title)
                .context(context)
                .videoLink(videoLink)
                .uploaderId(user)
                .Replies(new ArrayList<>())
                .build();
    }
}
