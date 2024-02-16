package com.nat.geeklolspring.admin.dto.Response.List;

import com.nat.geeklolspring.admin.dto.Response.AdminPageShortsResponseDTO;
import com.nat.geeklolspring.admin.dto.Response.AdminPageUserResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPageShortsListResponseDTO {

    private String error;
    private List<AdminPageShortsResponseDTO> shorts;


    private int totalPages; // 총 페이지 수
    private long totalCount; // 총 댓글 수

}
