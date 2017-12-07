package com.arkaces.aces_server.aces_listener.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    EventEntity findOneByTransactionId(String transactionId);

}
