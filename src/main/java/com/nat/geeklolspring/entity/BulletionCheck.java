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
@Table(name = "bulletion_check")
public class BulletionCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletion_check_id")
    private Long bulletionId;

    @Builder.Default
    @Column(name = "check_good")
    private int good = 0;


    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletion_checker_id")
    private User bulletionCheckerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletion_id")
    private BoardBulletin boardBulletinId;
}
