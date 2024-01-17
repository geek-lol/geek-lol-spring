package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;

import java.time.LocalDateTime;

import static com.nat.geeklolspring.entity.Auth.COMMON;

@Setter @Getter @ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
public class User {
    @Id
    @Column(name = "user_id")
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(name = "user_name", length = 40, nullable = false)
    private String userName;

    @CreationTimestamp
    @Column(name = "user_create_date", updatable = false)
    private LocalDateTime createdDate;

    @Builder.Default
    @Column(name = "user_report_count")
    private int reportCount = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private Auth auth = COMMON;
}
