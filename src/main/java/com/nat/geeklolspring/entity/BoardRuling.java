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
@Table(name = "board_ruling")
public class BoardRuling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruling_id")
    private Long rulingId;

    @Column(name = "ruling_link")
    private String rulingLink;

    @CreationTimestamp
    @Column(name = "ruling_date", updatable = false)
    private LocalDateTime rulingDate;

    @Builder.Default
    @Column(name = "view_count")
    private int viewCount = 0;

    @Column(name = "ruling_content")
    private String content;

    @Column(name = "ruling_title")
    private String title;

    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "poster_id")
    private User rulingPosterId;

    // Reply와의 OneToMany 관계 설정
    @OneToMany(mappedBy = "id")
    private List<RulingReply> Replies = new ArrayList<>();

    // Vote와의 OneToMany 관계 설정
    @OneToMany(mappedBy = "rulingVoteId")
    private List<RulingCheck> votes = new ArrayList<>();
}
