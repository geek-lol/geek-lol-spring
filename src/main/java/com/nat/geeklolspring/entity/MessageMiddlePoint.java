package com.nat.geeklolspring.entity;

import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@ToString
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
@Builder

@Entity
@Table(name = "post_office")
public class MessageMiddlePoint {
    /*
     들어갈 거 message의 fk
     받는 user의 id
     보내는 user의 id
     */


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_office_id")
    private Long id;


    //---------------------------------------
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

}