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

    public AdminPageRulingResponseDTO(BoardRuling boardRuling) {
        this.title = boardRuling.getTitle();
        this.rulingDate = boardRuling.getRulingDate();
        this.applyPosterId = boardRuling.getRulingPosterId();
    }

}
