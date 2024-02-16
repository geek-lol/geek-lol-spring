package com.nat.geeklolspring.entity;

import lombok.*;

import javax.persistence.*;
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "vote_ruling_check")
public class RulingCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruling_vote_id")
    private Long rulingVoteId;

    @Builder.Default
    @Column(name = "vote_pros")
    private int pros = 0;

    @Builder.Default
    @Column(name = "vote_cons")
    private int cons = 0;

//    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voter_id")
    private User rulingVoter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ruling_id")
    private BoardRuling rulingId;
}
