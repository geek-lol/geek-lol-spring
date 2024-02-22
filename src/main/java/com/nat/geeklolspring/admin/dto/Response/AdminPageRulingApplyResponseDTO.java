package com.nat.geeklolspring.admin.dto.Response;

import com.nat.geeklolspring.entity.BoardApply;
import lombok.*;

import java.time.LocalDateTime;
@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPageRulingApplyResponseDTO {

    private Long applyId;
    private String title;
    private LocalDateTime localDateTime;
    private String applyPosterId;
    private String posterName;
    private int viewCount;
    private int upCount;

    public AdminPageRulingApplyResponseDTO(BoardApply boardApply){
        this.applyId = boardApply.getApplyId();
        this.title = boardApply.getTitle();
        this.localDateTime = boardApply.getApplyDate();
        this.applyPosterId = boardApply.getUserId().getId();
        this.posterName = boardApply.getUserId().getUserName();
        this.viewCount = boardApply.getViewCount();
        this.upCount = boardApply.getUpCount();
    }
}
