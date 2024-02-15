package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
public class BoardReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long replyId;

    @CreationTimestamp
    @Column(name = " board_reply_date")
    private LocalDateTime boardReplyDate;

    @Column(name = "reply_text", nullable = false)
    private String replyText;

    //fk
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "bulletin_id")
    private BoardBulletin bulletin; //댓글이 달릴 게시물 fk

    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_writer_id", updatable = false)
    private User writerUser;

}
