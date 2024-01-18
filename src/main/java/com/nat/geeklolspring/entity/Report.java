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
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_number")
    private int reportNumber;

    @Column(name = "report_title", nullable = false)
    private String reportTitle;

    @Column(name = "report_content", nullable = false)
    private String reportContent;


    @Column(name = "report_link")
    private String reportLink;

    //---------------------------------------

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "report_user_id")
    private User reportUserId;


}
