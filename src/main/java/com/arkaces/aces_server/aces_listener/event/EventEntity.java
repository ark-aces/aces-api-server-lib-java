package com.arkaces.aces_server.aces_listener.event;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String id;

    private String transactionId;

    @Column(columnDefinition="TEXT")
    private String data;

    private LocalDateTime createdAt;
}

