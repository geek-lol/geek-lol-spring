package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.nat.geeklolspring.entity.Auth.COMMON;

@Setter @Getter
@ToString(exclude = {"MessageMiddlePoint", "Report", "BoardBulletion", "BoardReply", "BoardShorts", "ShortsReply", "VoteCheck"})
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

    @Column(name = "user_name", length = 40, nullable = false)
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
    @Column(nullable = false)
    @Builder.Default
    private Auth auth = COMMON;


//-----------------------------------------
    @OneToMany(mappedBy = "sender")
    private List<MessageMiddlePoint> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<MessageMiddlePoint> receivedMessages = new ArrayList<>();

    @OneToMany(mappedBy = "reportUserId")
    private List<Report> reportUser = new ArrayList<>();

    @OneToMany(mappedBy = "posterId")
    private List<BoardBulletion> poster = new ArrayList<>();

    @OneToMany(mappedBy = "replyWriter")
    private List<BoardReply> replyWriter = new ArrayList<>();

    @OneToMany(mappedBy = "uploaderId")
    private List<BoardShorts> uploaderId = new ArrayList<>();

    @OneToMany(mappedBy = "writerId")
    private List<ShortsReply> writerId = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    private List<VoteCheck> receiver = new ArrayList<>();

    @OneToMany(mappedBy = "rulingPosterId")
    private List<BoardRuling> rulingPoster = new ArrayList<>();

    @OneToMany(mappedBy = "rulingVoter")
    private List<RulingCheck> rulingVoter = new ArrayList<>();

    @OneToMany(mappedBy = "rulingWriterId")
    private List<RulingReply> rulingWriter = new ArrayList<>();

    @OneToMany(mappedBy = "applyPosterId")
    private List<BoardRuling> applyPoster = new ArrayList<>();

}
