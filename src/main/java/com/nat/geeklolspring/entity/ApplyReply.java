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
@Table(name = "apply_reply")
public class ApplyReply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_reply_id")
    private Long id; //댓글 pk

    @Column(name = "apply_text")
    private String context;

    @CreationTimestamp
    @Column(name = "apply_reply_date", updatable = false)
    private LocalDateTime replyDate;

    @Builder.Default
    @Column(name = "apply_reply_modify")
    private int modify = 0; //수정 횟수

    @Builder.Default
    @Column(name = "apply_report_count")
    private int reportCount = 0; // 신고 횟수

    @Column(name = "apply_reply_writer")
    private String writerId; // 작성자 아이디

    @Column(name = "writer_name")
    private String writerName; // 작성자 닉네임

    @Column(name = "apply_id")
    private Long applyId; //댓글이 달릴 게시물 fk

//    // fk가 필요한 곳
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "apply_id")
//    private String applyId; //댓글이 달릴 게시물 fk
}
