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
    private Long replyId;
    private String writerId;
    private String context;
    private LocalDateTime replyDate;
    private int modify;

    public ShortsReplyResponseDTO(ShortsReply reply) {
        this.replyId = reply.getId();
        this.writerId = reply.getWriterId();
        this.context = reply.getContext();
        this.replyDate = reply.getReplyDate();
        this.modify = reply.getModify();
    }
}
