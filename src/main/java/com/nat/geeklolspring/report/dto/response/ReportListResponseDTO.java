package com.nat.geeklolspring.report.dto.response;

import com.nat.geeklolspring.entity.Report;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportListResponseDTO {
    private String error;
    private List<Report> reportList;

    private int totalPages; // 총 페이지 수
    private long totalCount; // 총 댓글 수
}
