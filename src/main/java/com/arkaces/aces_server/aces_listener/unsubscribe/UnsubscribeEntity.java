package com.arkaces.aces_server.aces_listener.unsubscribe;

import com.arkaces.aces_server.aces_listener.subscription.SubscriptionEntity;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.ZonedDateTime;

@Data
@Entity
public class UnsubscribeEntity {

    @Id
    @GeneratedValue
    private Long pid;

    private String id;

    private ZonedDateTime createdAt;

    @ManyToOne
    private SubscriptionEntity subscriptionEntity;
}
