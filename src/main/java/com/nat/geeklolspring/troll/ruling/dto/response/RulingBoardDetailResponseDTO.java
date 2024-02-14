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
    private String applyPosterName;
    private int viewCount;
    private int replyCount;

    public RulingBoardDetailResponseDTO(BoardApply boardApply) {
        this.rulingId = boardApply.getApplyId();
        this.rulingLink =boardApply.getApplyLink();
        this.rulingDate = boardApply.getApplyDate();
        this.title = boardApply.getTitle();
        this.content = boardApply.getContent();
        this.applyPosterId = boardApply.getUserId().getId();
        this.applyPosterName = boardApply.getUserId().getUserName();

    }
    public RulingBoardDetailResponseDTO(BoardRuling boardRuling) {
        this.rulingId = boardRuling.getRulingId();
        this.rulingLink =boardRuling.getRulingLink();
        this.rulingDate = boardRuling.getRulingDate();
        this.title = boardRuling.getTitle();
        this.content = boardRuling.getContent();
        this.applyPosterId = boardRuling.getRulingPosterId();
        this.viewCount = boardRuling.getViewCount();
        this.applyPosterName = boardRuling.getRulingPosterName();
    }
    public BoardRuling toEntity(){
        return BoardRuling.builder()
                .rulingPosterId(applyPosterId)
                .rulingPosterName(applyPosterName)
                .rulingLink(rulingLink)
                .rulingDate(rulingDate)
                .content(content)
                .title(title)
                .viewCount(viewCount)
                .build();
    }
}
