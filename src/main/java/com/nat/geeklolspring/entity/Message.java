package com.nat.geeklolspring.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder

@Entity
public class Message {

    @Column(name = "message_content", nullable = false)
    private String messageContent;

    @Column(name = "message_title", nullable = false)
    private String messageTitle;

    @CreationTimestamp
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "sender_delete")
    private int senderDelete;

    @Column(name = "receiver_delete")
    private int receiverDelete;


    //----------------------------------------
    @Column(name = "sender_id")
    private String senderId;

    @Column(name = "receiver_id", nullable = false)
    private long receiverId;


}
