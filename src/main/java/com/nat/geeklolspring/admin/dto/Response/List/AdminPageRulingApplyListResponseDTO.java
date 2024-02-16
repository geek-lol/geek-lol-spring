package com.nat.geeklolspring.admin.dto.Response.List;

import com.nat.geeklolspring.admin.dto.Response.AdminPageRulingApplyResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPageRulingApplyListResponseDTO {

    private String error;
    private List<AdminPageRulingApplyResponseDTO> apply;


    private int totalPages;
    private long totalCount;

}
