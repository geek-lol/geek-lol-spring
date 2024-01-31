package com.nat.geeklolspring.report.dto.request;

import com.nat.geeklolspring.auth.TokenUserInfo;
import com.nat.geeklolspring.entity.Report;
import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportPostRequestDTO {
    @NotBlank
    private String reportTitle;

    private String reportContext;

    private String videoLink;

    public Report toEntity(TokenUserInfo userInfo) {
        return Report
                .builder()
                .userId(userInfo.getUserId())
                .userName(userInfo.getUserName())
                .reportTitle(reportTitle)
                .reportContent(reportContext)
                .reportLink(videoLink)
                .build();
    }
}
