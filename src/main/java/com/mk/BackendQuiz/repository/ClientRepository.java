package com.mk.BackendQuiz.repository;

import com.mk.BackendQuiz.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);

    Optional<Client> findByMobile(String mobile);

    Optional<Client> findOneByActivationKey(String activationKey);
}
