package com.nat.geeklolspring.troll.apply.dto.response;

import com.nat.geeklolspring.entity.BoardApply;
import com.nat.geeklolspring.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RulingApplyDetailResponseDTO {
    private Long applyId;
    private String applyLink;
    private String content;
    private String title;
    private int upCount;
    private int reportCount;
    private LocalDateTime localDateTime;
    private String posterId;
    private String posterName;
    private int viewCount;
    private int replyCount;

    public RulingApplyDetailResponseDTO(BoardApply boardApply){
        this.applyId = boardApply.getApplyId();
        this.applyLink = boardApply.getApplyLink();
        this.content = boardApply.getContent();
        this.title = boardApply.getTitle();
        this.upCount = boardApply.getUpCount();
        this.localDateTime = boardApply.getApplyDate();
        this.posterId = boardApply.getUserId().getId();
        this.posterName = boardApply.getUserId().getUserName();
        this.viewCount = boardApply.getViewCount();
    }
    public RulingApplyDetailResponseDTO(BoardApply boardApply,int replyCount){
        this.applyId = boardApply.getApplyId();
        this.applyLink = boardApply.getApplyLink();
        this.content = boardApply.getContent();
        this.title = boardApply.getTitle();
        this.upCount = boardApply.getUpCount();
        this.localDateTime = boardApply.getApplyDate();
        this.posterId = boardApply.getUserId().getId();
        this.posterName = boardApply.getUserId().getUserName();
        this.viewCount = boardApply.getViewCount();
        this.replyCount = replyCount;
    }
}
