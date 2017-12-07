package com.arkaces.aces_server.aces_listener.subscription_event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SubscriptionEventRepository extends JpaRepository<SubscriptionEventEntity, Long> {

    @Query(
        "select se from SubscriptionEventEntity se " +
        "where se.subscriptionEntity.pid = :pid " +
        "and se.eventEntity.transactionId = :transactionId"
    )
    SubscriptionEventEntity findOne(
            @Param("pid") Long pid,
            @Param("transactionId") String transactionId
    );

    List<SubscriptionEventEntity> findAllByStatus(String status);

}
