package com.nat.geeklolspring.admin.dto.Response.List;

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
public class AdminPageUserListResponseDTO {

    private String error;
    private List<AdminPageUserResponseDTO> user;


    private int totalPages;
    private long totalCount;

}
