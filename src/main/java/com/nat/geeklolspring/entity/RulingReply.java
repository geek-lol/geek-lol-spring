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
@Table(name = "ruling_reply")
public class RulingReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruling_reply_id")
    private Long id;

    @Column(name = "reply_ruling_text")
    private String context;

    @CreationTimestamp
    @Column(name = "ruling_reply_date", updatable = false)
    private LocalDateTime replyDate;

    @Builder.Default
    @Column(name = "ruling_reply_modify")
    private int modify = 0;

    @Column(name = "ruling_reply_writer")
    private String writerId; // 작성자 아이디

    @Column(name = "writer_name")
    private String writerName; // 작성자 닉네임

    @Column(name = "ruling_id")
    private Long rulingId;

    @Builder.Default
    @Column(name = "apply_report_count")
    private int reportCount = 0; // 신고 횟수
//    // fk가 필요한 곳
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User rulingWriterId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "ruling_id")
//    private BoardRuling rulingId;
}
