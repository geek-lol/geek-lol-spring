package com.nat.geeklolspring.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString(exclude = "applyId")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "apply_vote_check")
public class VoteApply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

//    @Column(name = "apply_id")
//    private Long applyId;

    @Builder.Default
    @Column(name = "vote_up")
    private int up = 1;

    //fk
    @ManyToOne
    @JoinColumn(name = "apply_id", referencedColumnName = "apply_id")
    private BoardApply applyId;

    //작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false)
    private User receiver;
}
