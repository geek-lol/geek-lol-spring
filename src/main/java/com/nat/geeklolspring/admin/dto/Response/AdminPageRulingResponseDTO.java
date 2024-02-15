package com.nat.geeklolspring.admin.dto.Response;

import com.nat.geeklolspring.entity.BoardRuling;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPageRulingResponseDTO {

    private Long rulingId;
    private LocalDateTime rulingDate;
    private String title;
    private String applyPosterId;
    private String applyPosterName;
    private int viewCount;

    public AdminPageRulingResponseDTO(BoardRuling boardRuling) {
        this.rulingId = boardRuling.getRulingId();
        this.title = boardRuling.getTitle();
        this.rulingDate = boardRuling.getRulingDate();
        this.applyPosterId = boardRuling.getRulingPosterId().getId();
        this.applyPosterName = boardRuling.getRulingPosterId().getUserName();
        this.viewCount = boardRuling.getViewCount();
    }

}
