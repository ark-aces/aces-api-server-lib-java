package com.arkaces.aces_server.aces_listener.unsubscribe;

import com.arkaces.aces_server.aces_listener.subscription.SubscriptionEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "unsubscribes")
public class UnsubscribeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String id;

    private LocalDateTime createdAt;

    @ManyToOne
    private SubscriptionEntity subscriptionEntity;
}
