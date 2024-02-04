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
public class CurrentBoardListResponseDTO {

    // 이전 투표 게시물
    private RulingBoardDetailResponseDTO previousBoard;
    //현재 투표중인 게시물
    private RulingBoardDetailResponseDTO currentBoard;

    public CurrentBoardListResponseDTO(List<BoardRuling> boardRulingList){
       this.previousBoard = new RulingBoardDetailResponseDTO(boardRulingList.get(0));
       this.currentBoard = new RulingBoardDetailResponseDTO(boardRulingList.get(1));
    }
}
