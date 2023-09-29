package com.mk.BackendQuiz.controller;

import com.mk.BackendQuiz.dto.Client.ClientAuthDto;
import com.mk.BackendQuiz.dto.Client.ClientCreateDto;
import com.mk.BackendQuiz.dto.response.Response;
import com.mk.BackendQuiz.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping(value = "/register")
    public Response register(@RequestBody @Valid ClientCreateDto clientCreateDto) {
        return Response.ok().setPayload(authService.register(clientCreateDto));
    }

    @GetMapping(value = "/activate")
    public void activateAccount(@RequestParam(value = "key") String key) {
        authService.activateRegistration(key);
    }

    @PostMapping(value = "/authenticate")
    public Response authenticate(@RequestBody ClientAuthDto clientAuthDto) {
        return Response.ok().setPayload(authService.authenticate(clientAuthDto));
    }

    @PostMapping(value = "/setPassword")
    public Response setPassword(@RequestBody @Valid ClientAuthDto clientAuthDto) {
        return Response.ok().setPayload(authService.setPassword(clientAuthDto));
    }
}
