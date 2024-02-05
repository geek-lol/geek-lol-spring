package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.nat.geeklolspring.entity.Role.COMMON;

@Setter @Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder

@Entity
public class User {

    @Id
    @Column(name = "user_id",unique = true,nullable = false)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(name = "user_name", length = 15, nullable = false)
    private String userName;

    @Column(name = "profile_image")
    private String profileImage;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime joinMembershipDate;

    @Builder.Default
    @Column(name = "user_report_count")
    private int reportCount = 0;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Role role = COMMON;

    @Column(name = "auto_login")
    @Builder.Default
    boolean autoLogin = false;


//-----------------------------------------
//    @OneToMany(mappedBy = "reportUserId")
//    private List<Report> reportUser = new ArrayList<>();

    @OneToMany(mappedBy = "bulletinCheckerId")
    private List<BulletinCheck> bulletinCheckerId = new ArrayList<>();

    @OneToMany(mappedBy = "rulingPosterId")
    private List<BoardRuling> rulingPoster = new ArrayList<>();

    @OneToMany(mappedBy = "rulingVoter")
    private List<RulingCheck> rulingVoter = new ArrayList<>();

    @OneToMany(mappedBy = "rulingWriterId")
    private List<RulingReply> rulingWriter = new ArrayList<>();

    @OneToMany(mappedBy = "applyPosterId")
    private List<BoardApply> applyPosterId = new ArrayList<>();

}
