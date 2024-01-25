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
    private String error; // 에러 발생 시 에러로그가 전달되는 곳
    private List<ShortsDetailResponseDTO> shorts; // Shorts 전체 리스트
}
