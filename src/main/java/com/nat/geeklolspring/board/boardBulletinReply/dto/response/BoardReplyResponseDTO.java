package com.nat.geeklolspring.board.boardBulletinReply.dto.response;

import com.nat.geeklolspring.entity.BoardReply;
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
public class BoardReplyResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private Long replyId;
    private String writerId;
    private String writerName;
    private String context;
    private LocalDateTime replyDate;
    public BoardReplyResponseDTO(BoardReply reply) {
        this.replyId = reply.getReplyId();
        this.writerId = reply.getReplyWriter().getId();
        this.writerName = reply.getReplyWriter().getUserName();
        this.context = reply.getReplyText();
        this.replyDate = reply.getBoardReplyDate();
    }
}
