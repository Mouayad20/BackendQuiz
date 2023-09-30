package com.mk.BackendQuiz.controller;

import com.mk.BackendQuiz.dto.Challenge1.Challenge1InputDto;
import com.mk.BackendQuiz.dto.Challenge1.Challenge1OutputDto;
import com.mk.BackendQuiz.dto.Client.ClientAuthDto;
import com.mk.BackendQuiz.dto.Client.ClientCreateDto;
import com.mk.BackendQuiz.dto.response.Response;
import com.mk.BackendQuiz.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    /*
        Write a Java program to solve this challenge. The program should find all unique triplets in the given
        array that sum up to the target sum.
        Input: array, expected sum
        Output: the array index of the two numbers whose sum equals the expected sum.

        I think there is a typo in whether you want the output as triples or doubles.
        So I solved the problem by considering that the desired output is triples
     */
    @GetMapping(value = "/challenge1")
    public Challenge1OutputDto challenge1(@RequestBody Challenge1InputDto challenge1InputDto) {
        return new Challenge1OutputDto().setIndexes(challenge1Function(challenge1InputDto));
    }

    public static List<List<Integer>> challenge1Function(Challenge1InputDto challenge1InputDto) {
        return Arrays.stream(challenge1InputDto.getArray())
                .distinct()
                .boxed()
                .flatMap(a -> Arrays.stream(challenge1InputDto.getArray())
                        .distinct()
                        .boxed()
                        .flatMap(b -> Arrays.stream(challenge1InputDto.getArray())
                                .distinct()
                                .boxed()
                                .filter(c -> a + b + c == challenge1InputDto.getExpectedSum() && a < b && b < c)
                                .map(c -> Arrays.asList(a, b, c))
                        )
                )
                .collect(Collectors.toList());
    }

}
