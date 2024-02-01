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

    @Column(name = "ruling_content")
    private String content;

    @Column(name = "ruling_title")
    private String title;

    @Builder.Default
    @Column(name = "vote_one")
    private int upCount = 0;

    @Builder.Default
    @Column(name = "ruling_status")
    private int status = 0;


    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rulingPosterId")
    private User rulingPosterId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "boardApplyId")
    private BoardApply boardApply;

    @OneToMany(mappedBy = "rulingId")
    private List<RulingReply> rulingReplyId = new ArrayList<>();

    @OneToMany(mappedBy = "rulingId")
    private List<RulingCheck> ruling_vote = new ArrayList<>();
}
