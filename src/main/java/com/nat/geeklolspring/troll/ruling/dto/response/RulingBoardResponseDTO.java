package com.nat.geeklolspring.troll.ruling.dto.response;

import com.nat.geeklolspring.entity.BoardRuling;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class RulingBoardResponseDTO {
    private Long rulingId;
    private String title;
    private String applyPosterId;

    public RulingBoardResponseDTO(BoardRuling boardRuling){
        this.rulingId = boardRuling.getRulingId();
        this.title = boardRuling.getTitle();
        this.applyPosterId = boardRuling.getRulingPosterId();
    }
}
