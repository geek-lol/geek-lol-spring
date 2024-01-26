package com.nat.geeklolspring.shorts.shortsreply.dto.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortsReplyListResponseDTO {
    private String error; // 에러 발생 시 에러로그가 전달되는 곳
    private List<ShortsReplyResponseDTO> reply; // reply 전체 리스트
}
