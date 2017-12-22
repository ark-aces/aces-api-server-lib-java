package com.arkaces.aces_server.ark_auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    AccountEntity findOneByStatusAndToken(String status, String token);

    List<AccountEntity> findAllByStatusIn(List<String> statusList);
    
    AccountEntity findOneById(String id);
}