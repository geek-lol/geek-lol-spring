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
}
