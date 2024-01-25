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
    private long replyId;

    @CreationTimestamp
    @Column(name = " board_reply_date")
    private LocalDateTime boardReplyDate;

    @Column(name = "reply_text", nullable = false)
    private String replyText;

    @Column(name = "board_reply_modify")
    private int boardReplyModify;



    //----------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reply_writer")
    private User replyWriter;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletion_id")
    private BoardBulletin bulletionId;


}
