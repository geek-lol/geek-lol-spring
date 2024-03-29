package com.nat.geeklolspring.troll.apply.dto.response;

import com.nat.geeklolspring.entity.ApplyReply;
import com.nat.geeklolspring.entity.BoardApply;
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
public class ApplyReplyResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private Long replyId;
    private String writerId;
    private String writerName;
    private String context;
    private LocalDateTime replyDate;
    private Long applyId;
    private String title;

    public ApplyReplyResponseDTO(ApplyReply reply) {
        this.replyId = reply.getId();
        this.writerId = reply.getUserId().getId();
        this.writerName = reply.getUserId().getUserName();
        this.context = reply.getContext();
        this.replyDate = reply.getReplyDate();
        this.applyId = reply.getApplyId().getApplyId();
    }
}
