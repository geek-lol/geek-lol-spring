package com.nat.geeklolspring.troll.apply.dto.response;

import com.nat.geeklolspring.shorts.shortsreply.dto.response.ShortsReplyResponseDTO;
import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplyReplyListResponseDTO {
    private String error; // 에러 발생 시 에러로그가 전달되는 곳
    private List<ApplyReplyResponseDTO> reply; // reply 전체 리스트

    // 페이징 처리할 때 필요한 함수
    private int totalPages; // 총 페이지 수
    private long totalCount; // 총 댓글 수
}
