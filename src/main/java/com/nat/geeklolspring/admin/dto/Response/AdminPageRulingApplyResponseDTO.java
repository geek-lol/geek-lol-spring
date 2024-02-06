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
    private int reportCount;
    private LocalDateTime apply_date;
    private String applyPosterId;

    public AdminPageRulingApplyResponseDTO(BoardApply boardApply){
        this.applyId = boardApply.getApplyId();
        this.title = boardApply.getTitle();
        this.reportCount = boardApply.getReportCount();
        this.apply_date = boardApply.getApplyDate();
        this.applyPosterId = boardApply.getApplyPosterId().toString();
    }
}
