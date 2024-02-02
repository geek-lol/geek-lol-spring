package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
@Setter
@Getter
@ToString (exclude = {"applyPosterId"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
@Table(name = "board_apply")
public class BoardApply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long applyId;

    @Column(name = "apply_link")
    private String applyLink;

    @CreationTimestamp
    @Column(name = "apply_date", updatable = false)
    private LocalDateTime applyDate;

    @Column(name = "apply_content")
    private String content;

    @Column(name = "apply_title")
    private String title;

    @Builder.Default
    @Column(name = "apply_report_count")
    private int reportCount = 0;

    @Builder.Default
    @Column(name = "check_good")
    private int upCount = 0;

    @Column(name = "applyPosterId")
    private String applyPosterId;
    // fk가 필요한 곳

    @OneToOne(mappedBy = "boardApply")
    private BoardRuling boardRuling;

}
