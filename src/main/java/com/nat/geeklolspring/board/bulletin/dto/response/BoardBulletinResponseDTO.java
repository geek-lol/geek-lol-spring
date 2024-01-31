package com.nat.geeklolspring.board.bulletin.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardBulletinResponseDTO {

    private String error;
    private List<BoardBulletinDetailResponseDTO> board;

}
