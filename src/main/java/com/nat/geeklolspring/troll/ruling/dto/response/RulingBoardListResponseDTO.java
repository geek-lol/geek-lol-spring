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

   private String error;
   private List<RulingBoardDetailResponseDTO> rulingList;

   // 페이징 처리할 때 필요한 함수
   private int totalPages; // 총 페이지 수
   private long totalCount; // 총 댓글 수

}
