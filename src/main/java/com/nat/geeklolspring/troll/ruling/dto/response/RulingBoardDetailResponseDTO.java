package com.nat.geeklolspring.troll.ruling.dto.response;

import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.User;
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
        this.rulingLink =boardRuling.getApplyId().getApplyLink();
        this.rulingDate = boardRuling.getApplyId().getApplyDate();
        this.title = boardRuling.getApplyId().getTitle();
        this.content = boardRuling.getApplyId().getContent();
        this.applyPosterId = boardRuling.getRulingPosterId().getId();
        this.viewCount = boardRuling.getViewCount();
        this.applyPosterName = boardRuling.getRulingPosterId().getUserName();
    }
    public RulingBoardDetailResponseDTO(BoardRuling boardRuling,int replyCount) {
        this.rulingId = boardRuling.getRulingId();
        this.rulingLink =boardRuling.getApplyId().getApplyLink();
        this.rulingDate = boardRuling.getApplyId().getApplyDate();
        this.title = boardRuling.getApplyId().getTitle();
        this.content = boardRuling.getApplyId().getContent();
        this.applyPosterId = boardRuling.getRulingPosterId().getId();
        this.viewCount = boardRuling.getViewCount();
        this.applyPosterName = boardRuling.getRulingPosterId().getUserName();
        this.replyCount = replyCount;
    }
    public BoardRuling toEntity(User user,BoardApply apply){
        return BoardRuling.builder()
                .rulingPosterId(user)
                .ApplyId(apply)
                .viewCount(viewCount)
                .build();
    }
}
