package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.nat.geeklolspring.entity.Role.COMMON;

@Setter @Getter
@ToString(exclude = {
        "bulletinList","rulingList","applyList","shortsList","csGameRanksList","resGameRankList"
        ,"rulingReplyList"
})
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

//    // board와의 OneToMany 관계 설정
//    @OneToMany(mappedBy = "bulletinId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BoardBulletin> bulletinList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "rulingId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BoardRuling> rulingList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "applyId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BoardApply> applyList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "shortsId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BoardShorts> shotsList = new ArrayList<>();
//
//    // game rank와의 관계설정
//    @OneToMany(mappedBy = "gameId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<CsGameRank> csGameRanksList = new ArrayList<>();
//
//    @OneToMany(mappedBy = "gameId", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ResGameRank> resGameRanksList = new ArrayList<>();




}
