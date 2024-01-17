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
public class BoardBulletion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletion_id")
    private long bulletionId;

    @CreationTimestamp
    @Column(name = "board_date")
    private LocalDateTime boardDate;

    @Column(nullable = false)
    private String title;

    @Column(name = "board_content", nullable = false)
    private String boardContent;

    @Column(name = "board_media")
    private String boardMedia;

    @Column(name = " board_report_count")
    private String boardReportCount;




    //----------------------------------------
    @Column(name = "poster_id")
    private String posterId;





}
