package com.mk.BackendQuiz.service;

import com.mk.BackendQuiz.dto.ClientAuthDto;
import com.mk.BackendQuiz.dto.ClientRegisterDto;
import com.mk.BackendQuiz.dto.JwtRequest;
import com.mk.BackendQuiz.dto.JwtResponse;
import com.mk.BackendQuiz.dto.response.Response;
import com.mk.BackendQuiz.exception.EntityType;
import com.mk.BackendQuiz.exception.ExceptionManager;
import com.mk.BackendQuiz.exception.ExceptionType;
import com.mk.BackendQuiz.model.Client;
import com.mk.BackendQuiz.repository.ClientRepository;
import com.mk.BackendQuiz.security.JwtTokenUtil;
import liquibase.repackaged.org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Optional;

import static com.mk.BackendQuiz.exception.EntityType.CLIENT;
import static com.mk.BackendQuiz.exception.ExceptionType.*;
import static com.mk.BackendQuiz.exception.ExceptionType.ENTITY_EXCEPTION;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserDetailsService jwtInMemoryUserDetailsService;

    public ClientRegisterDto register(ClientRegisterDto clientRegisterDto) {
        if (clientRepository.findByMobile(clientRegisterDto.getMobile()) != null)
            throw exception(CLIENT, DUPLICATE_ENTITY, " Mobile " + clientRegisterDto.getMobile());
        Client client = clientRepository.findByEmail(clientRegisterDto.getEmail());
        if (client == null) {

            client = new Client()
                    .setName(clientRegisterDto.getName())
                    .setLastName(clientRegisterDto.getLastName())
                    .setMobile(clientRegisterDto.getMobile())
                    .setEmail(clientRegisterDto.getEmail())
                    .setAddress(clientRegisterDto.getAddress())
                    .setActivationKey((int) Math.floor(Math.random() * (99998 - 10000 + 1) + 10000) + "")
                    .setPassword(RandomStringUtils.random(30, true, true));

            String subject = "Backend Quiz Verification Email";
            String text = "Your verification code is " + client.getActivationKey();

            try {
                emailService.sendEmail(client.getEmail(), subject, text);
            } catch (MessagingException e) {
                throw exception(CLIENT, ENTITY_EXCEPTION, " Verification Email Not Sent ");
            }

            return modelMapper.map(
                    clientRepository.save(client),
                    ClientRegisterDto.class
            );
        }
        throw exception(CLIENT, DUPLICATE_ENTITY, " Email " + clientRegisterDto.getEmail());
    }

    public void activateRegistration(String key) {
        Optional<Client> client = clientRepository
                .findOneByActivationKey(key)
                .map(client1 -> {
                    // activate given client for the registration key.
                    client1.setActivated(true);
                    client1.setActivationKey(null);
                    return clientRepository.save(client1);
                });
        if (client.isEmpty()) {
            throw exception(CLIENT, ENTITY_EXCEPTION, "No client was found for this activation key");
        }
    }

    public Boolean setPassword(ClientAuthDto clientAuthDto) {
        Client client = clientRepository.findByEmail(clientAuthDto.getEmail());
        if (client == null)
            throw exception(CLIENT, ENTITY_NOT_FOUND, clientAuthDto.getEmail());
        if (!clientRepository.findByEmail(clientAuthDto.getEmail()).isActivated())
            throw exception(CLIENT, ENTITY_EXCEPTION, "Client " + clientAuthDto.getEmail() + " Is not active ");

        client = clientRepository.save(
                client.setPassword(
                        bCryptPasswordEncoder.encode(clientAuthDto.getPassword()
                        )
                )
        );
        return !client.getPassword().equals(clientAuthDto.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Client client = clientRepository.findByEmail(email);
        if (client == null)
            throw exception(CLIENT, ENTITY_NOT_FOUND, email);
        return new User(client.getEmail(), client.getPassword(), new ArrayList<>());
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return ExceptionManager.throwException(entityType, exceptionType, args);
    }

    public Response authenticate(JwtRequest jwtRequest) {

        if (jwtRequest.getEmail() == null || jwtRequest.getPassword() == null)
            throw exception(CLIENT, ENTITY_EXCEPTION, "Email Or Password is Null");

        if (!clientRepository.findByEmail(jwtRequest.getEmail()).isActivated())
            throw exception(CLIENT, ENTITY_EXCEPTION, "Client " + jwtRequest.getEmail() + " Is not active ");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(), jwtRequest.getPassword()));
        } catch (DisabledException e) {
            throw exception(CLIENT, ENTITY_EXCEPTION, "USER_DISABLED " + e.getMessage());
        } catch (BadCredentialsException e) {
            throw exception(CLIENT, ENTITY_EXCEPTION, "INVALID_CREDENTIALS " + e.getMessage());
        }

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(jwtRequest.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);
        return Response.ok().setPayload(new JwtResponse(token));
    }
}
