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
@Table(name = "vote_check")
public class VoteCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private int voteId;

    @Builder.Default
    @Column(name = "vote_up")
    private int up = 0;

    @Builder.Default
    @Column(name = "vote_down")
    private int down = 0;





    // fk가 필요한 곳
    @Column(name = "receiver_id")
    private String receiver;

    @Column(name = "shorts_id")
    private String shortsId;
}
