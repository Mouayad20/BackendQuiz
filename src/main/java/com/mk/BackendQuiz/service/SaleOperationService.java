package com.mk.BackendQuiz.service;

import com.mk.BackendQuiz.repository.SaleOperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaleOperationService {
    @Autowired
    SaleOperationRepository saleOperationRepository;
}
