package com.mk.BackendQuiz.controller;


import com.mk.BackendQuiz.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    ClientService clientService;
}
