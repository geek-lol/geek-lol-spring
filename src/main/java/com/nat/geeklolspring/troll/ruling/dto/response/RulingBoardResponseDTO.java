package com.nat.geeklolspring.troll.ruling.dto.response;

import com.nat.geeklolspring.entity.BoardRuling;
import lombok.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RulingBoardResponseDTO {
    private Long rulingId;
    private String title;
    private String posterId;

    public RulingBoardResponseDTO(BoardRuling boardRuling){
        this.rulingId = boardRuling.getRulingId();
        this.title = boardRuling.getTitle();
        this.posterId = boardRuling.getRulingPosterId().getId();
    }
}
