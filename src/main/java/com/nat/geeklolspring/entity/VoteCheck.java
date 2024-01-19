package com.nat.geeklolspring.entity;

import com.nat.geeklolspring.entity.BoardShorts;
import com.nat.geeklolspring.entity.User;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString(exclude = {"user", "boardShorts"})
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

    @Column(name = "user_id")
    private String receiver;

    @Column(name = "shorts_id")
    private Long shortsId;

    @Builder.Default
    @Column(name = "vote_up")
    private int up = 0;





    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shorts_id", insertable = false, updatable = false)
    private BoardShorts boardShorts;
}
