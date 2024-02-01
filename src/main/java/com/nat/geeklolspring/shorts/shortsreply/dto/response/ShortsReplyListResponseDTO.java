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

    // 페이징 처리할 때 필요한 함수
    private int totalPages; // 총 페이지 수
    private long totalCount; // 총 댓글 수
}
