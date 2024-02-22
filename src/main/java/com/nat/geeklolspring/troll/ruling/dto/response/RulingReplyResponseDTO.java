package com.nat.geeklolspring.troll.ruling.dto.response;

import com.nat.geeklolspring.entity.BoardRuling;
import com.nat.geeklolspring.entity.RulingReply;
import com.nat.geeklolspring.entity.User;
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
    private String userId;
    private String context;
    private LocalDateTime replyDate;
    private Long rulingId;
    private String title;
    private String userName;

    public RulingReplyResponseDTO(RulingReply reply) {
        this.replyId = reply.getId();
        this.context = reply.getContext();
        this.replyDate = reply.getReplyDate();
        this.rulingId = reply.getRulingId().getRulingId();
        this.userId = reply.getRulingWriterId().getId();
        this.userName = reply.getRulingWriterId().getUserName();


    }
}
