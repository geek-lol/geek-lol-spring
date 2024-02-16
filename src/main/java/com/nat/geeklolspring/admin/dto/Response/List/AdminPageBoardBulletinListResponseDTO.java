package com.nat.geeklolspring.admin.dto.Response.List;

import com.nat.geeklolspring.admin.dto.Response.AdminPageBoardBulletinResponseDTO;
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
public class AdminPageBoardBulletinListResponseDTO {

    private String error;
    private List<AdminPageBoardBulletinResponseDTO> board;


    private int totalPages;
    private long totalCount;

}
