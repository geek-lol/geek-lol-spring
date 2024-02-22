package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    private Long bulletinCheckId;

    @Builder.Default
    @Column(name = "check_good")
    private int good = 0;

    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "bulletin_id")
    private BoardBulletin bulletin;
}
