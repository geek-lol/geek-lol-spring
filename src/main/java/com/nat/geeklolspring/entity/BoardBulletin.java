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
public class BoardBulletin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bulletin_id")
    private Long bulletinId;

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
    @Builder.Default
    private int boardReportCount = 0;


    @Builder.Default
    @Column(name = "view_count")
    private int viewCount = 0;

    @Builder.Default
    @Column(name = "up_count")
    private int upCount = 0;

//    @Column(name = "poster_id")
//    private String posterId;

//    @Column(name = "poster_name")
//    private String posterName;



    //----------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private User posterId;


    //----------------------------------------

    @OneToMany(mappedBy = "boardBulletinId")
    private List<BoardReply> boardReply = new ArrayList<>();


    @OneToMany(mappedBy = "boardBulletinId")
    private List<BulletinCheck> boardBulletinId = new ArrayList<>();


}
