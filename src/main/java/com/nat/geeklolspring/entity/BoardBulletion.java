package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private User posterId;



    //----------------------------------------

    @OneToMany(mappedBy = "bulletionId")
    private List<BoardReply> boardReply = new ArrayList<>();


}
