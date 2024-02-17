package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "apply_id")
    private BoardApply applyId; //댓글이 달릴 게시물 fk

    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", updatable = false)
    private User userId;

}
