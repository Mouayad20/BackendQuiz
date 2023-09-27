package com.mk.BackendQuiz.repository;

import com.mk.BackendQuiz.model.SaleOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleOperationRepository extends JpaRepository<SaleOperation, Long> {
}