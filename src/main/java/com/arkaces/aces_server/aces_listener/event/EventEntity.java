package com.arkaces.aces_server.aces_listener.event;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue
    private Long pid;

    private String id;

    private String transactionId;

    @Column(columnDefinition="TEXT")
    private String data;

    private ZonedDateTime createdAt;
}

