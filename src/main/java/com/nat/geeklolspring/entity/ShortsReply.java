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
@Table(name = "shorts_reply")
public class ShortsReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id")
    private Long id; // 댓글 고유 Id

    @Column(name = "user_id")
    private String writerId; // 작성자

    @Column(name = "user_name")
    private String writerName; // 작성자 닉네임

    @Column(name = "shorts_id")
    private Long shortsId; // 해당 댓글이 쓰인 쇼츠의 Id

    @Column(name = "reply_text")
    private String context; // 댓글 내용

    @CreationTimestamp
    @Column(name = "shorts_reply_date")
    private LocalDateTime replyDate; // 작성날짜

    @Builder.Default
    @Column(name = "shorts_reply_modify")
    private int modify = 0; // 댓글 수정 횟수





    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shorts_id", insertable = false, updatable = false)
    private BoardShorts boardShorts;
}
