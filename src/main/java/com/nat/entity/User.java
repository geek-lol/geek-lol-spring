package com.nat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private String id;

    @Column(name = "password")
    private String password;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_report_count")
    private int userReportCount;

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "auth")
    private Auth auth;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime joinMembershipDate;


}
