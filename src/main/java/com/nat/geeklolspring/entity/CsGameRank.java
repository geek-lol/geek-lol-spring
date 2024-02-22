package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "csgame_rank")
public class CsGameRank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "csgame_id")
    private Long gameId;

    @Column(name = "csgame_score")
    private float score;

    @CreationTimestamp
    @Column(name = "record_date", updatable = false)
    private LocalDateTime recordDate;

    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private User user;


}
