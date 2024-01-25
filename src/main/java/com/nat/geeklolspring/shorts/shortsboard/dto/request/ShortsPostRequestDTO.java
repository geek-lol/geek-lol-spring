package com.nat.geeklolspring.shorts.shortsboard.dto.request;

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
    private String title;
    private String uploaderId;
    private MultipartFile videoLink;

    public BoardShorts toEntity(String videoLink) {
        return BoardShorts.builder()
                .title(title)
                .uploaderId(uploaderId)
                .videoLink(videoLink)
                .build();
    }
}
