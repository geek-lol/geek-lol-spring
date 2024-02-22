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
@ToString(exclude = {"votes","Replies"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
public class BoardBulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_id")
    private Long bulletinId;

    @CreationTimestamp
    @Column(name = "board_date")
    private LocalDateTime boardDate;

    @Column(nullable = false)
    private String title;

    @Column(name = "board_content", nullable = false)
    private String boardContent;

    @Column(name = "board_media")
    private String boardMedia;

    @Builder.Default
    @Column(name = "view_count")
    private int viewCount = 0;

    @Builder.Default
    @Column(name = "up_count")
    private int upCount = 0;

    //---------------------------------------
    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    // Reply와의 OneToMany 관계 설정
    @OneToMany(mappedBy = "replyId", cascade = CascadeType.PERSIST)
    private List<BoardReply> Replies = new ArrayList<>();

    // Vote와의 OneToMany 관계 설정
    @OneToMany(mappedBy = "bulletinCheckId", cascade = CascadeType.PERSIST)
    private List<BulletinCheck> votes = new ArrayList<>();

}
