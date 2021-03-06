package com.arkaces.aces_server.aces_listener.subscription_event;

import com.arkaces.aces_server.aces_listener.event.EventEntity;
import com.arkaces.aces_server.aces_listener.subscription.SubscriptionEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "subscription_events")
public class SubscriptionEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String status;

    private Integer tries;

    @OneToOne
    private SubscriptionEntity subscriptionEntity;

    @OneToOne
    private EventEntity eventEntity;

    private LocalDateTime createdAt;
}
