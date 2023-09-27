package com.mk.BackendQuiz.controller;

import com.mk.BackendQuiz.service.SaleOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/saleOperations")
public class SaleOperationController {
    @Autowired
    SaleOperationService saleOperationService;
}
