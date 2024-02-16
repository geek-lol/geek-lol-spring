package com.nat.geeklolspring.admin.dto.Response.List;

import com.nat.geeklolspring.admin.dto.Response.AdminPageRulingResponseDTO;
import com.nat.geeklolspring.admin.dto.Response.AdminPageShortsResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPageRulingListResponseDTO {


    private String error;
    private List<AdminPageRulingResponseDTO> ruling;


    private int totalPages;
    private long totalCount;


}
