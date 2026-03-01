package org.example.stub.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name="messages")
@Data


public class MessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "msgId", nullable = false,length = 36)
    private String msgId;

    @Column(name = "fullName", nullable = false)
    private  String fullName;

    @Column(name = "inn", nullable = false, length = 12)
    private String inn;

    @Column(name = "time", nullable = false)
    private LocalDateTime time;
}













