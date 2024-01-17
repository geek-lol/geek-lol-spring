package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

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
@Table(name = "board_shorts")
public class BoardShorts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shorts_id")
    private String id;

    @Column(name = "shorts_link")
    private String videoLink;

    @CreationTimestamp
    @Column(name = "shorts_date", updatable = false)
    private LocalDateTime uploadDate;

    @Column(name = "shorts_title")
    private String title;

    @Builder.Default
    @Column(name = "vote_up")
    private int upCount = 0;

    @Builder.Default
    @Column(name = "vote_down")
    private int downCount = 0;

    @Builder.Default
    @Column(name = "shorts_report_count")
    private int reportCount = 0;





    // fk가 필요한 곳
    @Column(name = "poster_id")
    private String uploaderId;
}
