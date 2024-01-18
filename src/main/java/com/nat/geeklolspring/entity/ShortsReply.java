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
    private Long id;

    @Column(name = "reply_text")
    private String context;

    @CreationTimestamp
    @Column(name = "shorts_reply_date", updatable = false)
    private LocalDateTime replyDate;

    @Builder.Default
    @Column(name = "shorts_reply_modify")
    private int modify = 0;





    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User writerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shorts_id")
    private BoardShorts shortsId;
}
