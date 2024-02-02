package com.nat.geeklolspring.troll.dto.response;

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
}
