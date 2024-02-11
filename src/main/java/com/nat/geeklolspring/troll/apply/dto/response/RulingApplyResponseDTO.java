package com.nat.geeklolspring.troll.apply.dto.response;

import com.nat.geeklolspring.entity.BoardApply;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RulingApplyResponseDTO {
    private String error;
    private List<RulingApplyDetailResponseDTO> boardApply;

    // 페이징 처리할 때 필요한 함수
    private int totalPages; // 총 페이지 수
    private long totalCount; // 총 댓글 수
}
