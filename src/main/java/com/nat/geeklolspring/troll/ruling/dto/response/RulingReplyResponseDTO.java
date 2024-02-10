package com.nat.geeklolspring.troll.ruling.dto.response;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.RulingReply;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RulingReplyResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private Long replyId;
    private String writerId;
    private String writerName;
    private String context;
    private LocalDateTime replyDate;
    private Long rulingId;
    private String title;
    private int modify;

    public RulingReplyResponseDTO(RulingReply reply) {
        this.replyId = reply.getId();
        this.writerId = reply.getWriterId();
        this.writerName = reply.getWriterName();
        this.context = reply.getContext();
        this.replyDate = reply.getReplyDate();
        this.modify = reply.getModify();
        this.rulingId = reply.getRulingId();
    }
}
