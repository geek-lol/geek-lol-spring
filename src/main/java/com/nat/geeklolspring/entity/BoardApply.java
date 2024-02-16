package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString(exclude = {"votes","applyReplies"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "board_apply")
public class BoardApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long applyId;

    @Column(name = "apply_link")
    private String applyLink;

    @CreationTimestamp
    @Column(name = "apply_date", updatable = false)
    private LocalDateTime applyDate;

    @Column(name = "apply_content")
    private String content;

    @Column(name = "apply_title")
    private String title;

    @Builder.Default
    @Column(name = "check_good")
    private int upCount = 0;

    @Builder.Default
    @Column(name = "view_count")
    private int viewCount = 0;

    // fk가 필요한 곳
    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User userId;

    // ApplyReply와의 OneToMany 관계 설정
    @OneToMany(mappedBy = "applyId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApplyReply> applyReplies = new ArrayList<>();

    // Vote와의 OneToMany 관계 설정
    @OneToMany(mappedBy = "voteId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VoteApply> votes = new ArrayList<>();
}
