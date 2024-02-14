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
@Table(name = "board_ruling")
public class BoardRuling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ruling_id")
    private Long rulingId;

//    @Column(name = "ruling_link")
//    private String rulingLink;
//
//    @CreationTimestamp
//    @Column(name = "ruling_date", updatable = false)
//    private LocalDateTime rulingDate;

    @Builder.Default
    @Column(name = "view_count")
    private int viewCount = 0;

//    @Column(name = "ruling_content")
//    private String content;
//
//    @Column(name = "ruling_title")
//    private String title;

    // fk가 필요한 곳
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id")
    private User rulingPosterId;
//
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apply_id", referencedColumnName = "apply_id")
    private BoardApply ApplyId;
//
//    @OneToMany(mappedBy = "rulingId")
//    private List<RulingReply> rulingReplyId = new ArrayList<>();

}
