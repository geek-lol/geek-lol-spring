package com.nat.geeklolspring.shorts.dto.response;

import com.nat.geeklolspring.entity.BoardShorts;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortsListResponseDTO {
    private String error;
    private List<ShortsDetailResponseDTO> shorts;
}
