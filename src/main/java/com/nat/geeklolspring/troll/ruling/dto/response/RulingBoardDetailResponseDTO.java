package com.nat.geeklolspring.troll.ruling.dto.response;

import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.BoardRuling;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RulingBoardDetailResponseDTO {
    private Long rulingId;
    private String rulingLink;
    private LocalDateTime rulingDate;
    private String content;
    private String title;
    private String applyPosterId;

    public RulingBoardDetailResponseDTO(BoardApply boardApply) {
        this.rulingLink =boardApply.getApplyLink();
        this.rulingDate = boardApply.getApplyDate();
        this.title = boardApply.getTitle();
        this.content = boardApply.getContent();
        this.applyPosterId = boardApply.getApplyPosterId();
    }
    public RulingBoardDetailResponseDTO(BoardRuling boardRuling) {
        this.rulingLink =boardRuling.getRulingLink();
        this.rulingDate = boardRuling.getRulingDate();
        this.title = boardRuling.getTitle();
        this.content = boardRuling.getContent();
        this.applyPosterId = boardRuling.getRulingPosterId();
    }
    public BoardRuling toEntity(){
        return BoardRuling.builder()
                .rulingPosterId(applyPosterId)
                .rulingLink(rulingLink)
                .rulingDate(rulingDate)
                .content(content)
                .title(title)
                .build();
    }
}
