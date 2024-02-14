package com.nat.geeklolspring.shorts.shortsreply.dto.response;

import com.nat.geeklolspring.entity.ShortsReply;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShortsReplyResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private Long replyId;
    private String writerId;
    private String writerName;
    private String context;
    private LocalDateTime replyDate;

    public ShortsReplyResponseDTO(ShortsReply reply) {
        this.replyId = reply.getId();
        this.writerId = reply.getWriterId().getId();
        this.writerName = reply.getWriterId().getUserName();
        this.context = reply.getContext();
        this.replyDate = reply.getReplyDate();
    }
}
