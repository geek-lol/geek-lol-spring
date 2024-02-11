package com.nat.geeklolspring.troll.ruling.dto.response;

import com.nat.geeklolspring.entity.BoardRuling;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RulingBoardListResponseDTO {

   private List<RulingBoardDetailResponseDTO> rulingList;


}
