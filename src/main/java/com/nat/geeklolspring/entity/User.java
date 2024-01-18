package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.nat.geeklolspring.entity.Auth.COMMON;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
public class User {
    @Id
    @GeneratedValue(generator = "uid")
    @GenericGenerator(strategy = "uuid", name = "uid")
    @Column(name = "user_id")
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

    @OneToMany(mappedBy = "shortsWriter")
    private List<BoardShorts> shortsWriter = new ArrayList<>();

    @OneToMany(mappedBy = "shortsReplyWriter")
    private List<ShortsReply> shortsReplyWriter = new ArrayList<>();

    @OneToMany(mappedBy = "voteReceiver")
    private List<VoteCheck> voteReceiver = new ArrayList<>();


}
