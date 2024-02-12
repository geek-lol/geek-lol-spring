package com.nat.geeklolspring.board.boardBulletinReply.dto.response;

import com.nat.geeklolspring.entity.BoardReply;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardMyReplyResponseDTO {
    // 웹사이트에 보내줄(보여줄) 정보들
    private Long replyId;
    private String writerId;
    private String writerName;
    private String context;
    private LocalDateTime replyDate;
    private Long boardId;
    private String title;
    public BoardMyReplyResponseDTO(BoardReply reply) {
        this.replyId = reply.getReplyId();
        this.writerId = reply.getReplyWriterId();
        this.writerName = reply.getReplyWriterName();
        this.context = reply.getReplyText();
        this.replyDate = reply.getBoardReplyDate();
        this.boardId = reply.getBulletinId();
    }
}
