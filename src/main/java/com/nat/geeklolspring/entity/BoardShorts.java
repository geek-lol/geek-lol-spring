package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "board_shorts")
public class BoardShorts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shorts_id")
    private Long shortsId; // 쇼츠 고유 아이디, 자동생성

    @CreationTimestamp
    @Column(name = "shorts_date", updatable = false)
    private LocalDateTime uploadDate;

    @Column(name = "shorts_title", length = 50)
    private String title;

    @Column(name = "shorts_context", length = 100)
    private String context;

    @Column(name = "shorts_link")
    private String videoLink;

    @Builder.Default
    @Column(name = "reply_count")
    private int replyCount = 0;

    @Builder.Default
    @Column(name = "view_count")
    private int viewCount = 0;

    @Builder.Default
    @Column(name = "vote_up")
    private int upCount = 0;

    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User uploaderId;
}
