package com.arkaces.aces_server.aces_listener.subscription;

import com.arkaces.aces_server.aces_listener.unsubscribe.UnsubscribeEntity;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pid;

    private String id;
    private String callbackUrl;
    private Integer minConfirmations;
    private String status;
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "subscriptionEntity")
    private List<UnsubscribeEntity> unsubscribeEntities;

}
