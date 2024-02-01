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
@Table(name = "bulletin_check")
public class BulletinCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_check_id")
    private Long bulletinId;

    @Builder.Default
    @Column(name = "check_good")
    private int good = 0;


    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_checker_id")
    private User bulletinCheckerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bulletin_id")
    private BoardBulletin boardBulletinId;
}
