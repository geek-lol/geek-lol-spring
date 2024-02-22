package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Setter
@Getter
//@ToString(exclude = {"user", "boardShorts"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "vote_check")
public class VoteCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @Builder.Default
    @Column(name = "vote_up")
    private int up = 0;


    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "shorts_id", updatable = false)
    private BoardShorts shorts;
}
