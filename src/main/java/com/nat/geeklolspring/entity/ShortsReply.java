package com.nat.geeklolspring.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
@Table(name = "shorts_reply")
public class ShortsReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id; // 댓글 고유 Id

    @Column(name = "reply_text")
    private String context; // 댓글 내용

    @CreationTimestamp
    @Column(name = "shorts_reply_date")
    private LocalDateTime replyDate; // 작성날짜

    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", updatable = false)
    private User writerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "shorts_id", updatable = false)
    private BoardShorts shortsId;
}
