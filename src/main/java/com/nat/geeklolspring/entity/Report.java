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
    private Long reportNumber;

    @Column(name = "report_user")
    private String userId;

    @Column(name = "report_user_name")
    private String userName;

    @Column(name = "report_title", nullable = false)
    private String reportTitle;

    @Column(name = "report_content", nullable = false)
    private String reportContent;

    @Column(name = "report_link")
    private String reportLink;

    @Column(name = "report_check")
    @Builder.Default
    private int reportCheck = 0;

    //---------------------------------------

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "report_user_id")
//    private User reportUserId;


}
