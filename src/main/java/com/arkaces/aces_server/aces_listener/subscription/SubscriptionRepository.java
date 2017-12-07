package com.arkaces.aces_server.aces_listener.subscription;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface SubscriptionRepository extends JpaRepository<SubscriptionEntity, Long> {

    List<SubscriptionEntity> findAllByStatus(String status);

    SubscriptionEntity findOneById(String id);

}
