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
    private Long shortsId;

    @Column(name = "poster_id")
    private String uploaderId;

    @Column(name = "shorts_link")
    private String videoLink;

    @CreationTimestamp
    @Column(name = "shorts_date", updatable = false)
    private LocalDateTime uploadDate;

    @Column(name = "shorts_title")
    private String title;

    @Builder.Default
    @Column(name = "view_count")
    private int viewCount = 0;

    @Builder.Default
    @Column(name = "vote_up")
    private int upCount = 0;

    @Builder.Default
    @Column(name = "shorts_report_count")
    private int reportCount = 0;





    // fk가 필요한 곳
    @OneToMany(mappedBy = "shortsId")
    private List<ShortsReply> shortsReplyId = new ArrayList<>();

    @OneToMany(mappedBy = "shortsId")
    private List<VoteCheck> shortsVoteId = new ArrayList<>();
}
