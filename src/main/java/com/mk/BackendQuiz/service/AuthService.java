package com.mk.BackendQuiz.service;

import com.mk.BackendQuiz.dto.Client.ClientAuthDto;
import com.mk.BackendQuiz.dto.Client.ClientCreateDto;
import com.mk.BackendQuiz.dto.JwtResponse;
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
    private ClientRepository clientRepository;
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

    public ClientCreateDto register(ClientCreateDto clientCreateDto) {
        if (clientRepository.findByMobile(clientCreateDto.getMobile()).isPresent())
            throw exception(CLIENT, DUPLICATE_ENTITY, " Mobile " + clientCreateDto.getMobile());
        if (clientRepository.findByEmail(clientCreateDto.getEmail()).isEmpty()) {

            Client client = new Client()
                    .setName(clientCreateDto.getName())
                    .setLastName(clientCreateDto.getLastName())
                    .setMobile(clientCreateDto.getMobile())
                    .setEmail(clientCreateDto.getEmail())
                    .setAddress(clientCreateDto.getAddress())
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
                    ClientCreateDto.class
            );
        } else
            throw exception(CLIENT, DUPLICATE_ENTITY, " Email " + clientCreateDto.getEmail());
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
        Optional<Client> client = clientRepository.findByEmail(clientAuthDto.getEmail());
        if (client.isEmpty())
            throw exception(CLIENT, ENTITY_NOT_FOUND, clientAuthDto.getEmail());
        if (!client.get().isActivated())
            throw exception(CLIENT, ENTITY_EXCEPTION, "Client " + clientAuthDto.getEmail() + " Is not active ");
        client.get().setPassword(
                bCryptPasswordEncoder.encode(clientAuthDto.getPassword()
                )
        );
        Client savedClient = clientRepository.save(client.get());
        return !savedClient.getPassword().equals(clientAuthDto.getPassword());
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<Client> client = clientRepository.findByEmail(email);
        if (client.isEmpty())
            throw exception(CLIENT, ENTITY_NOT_FOUND, email);
        return new User(client.get().getEmail(), client.get().getPassword(), new ArrayList<>());
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return ExceptionManager.throwException(entityType, exceptionType, args);
    }

    public JwtResponse authenticate(ClientAuthDto clientAuthDto) {

        if (clientAuthDto.getEmail() == null || clientAuthDto.getPassword() == null)
            throw exception(CLIENT, ENTITY_EXCEPTION, "Email Or Password is Null");
        Optional<Client> client = clientRepository.findByEmail(clientAuthDto.getEmail());
        if (client.isEmpty())
            throw exception(CLIENT, ENTITY_NOT_FOUND, clientAuthDto.getEmail());
        if (!client.get().isActivated())
            throw exception(CLIENT, ENTITY_EXCEPTION, "Client " + clientAuthDto.getEmail() + " Is not active ");

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(clientAuthDto.getEmail(), clientAuthDto.getPassword()));
        } catch (DisabledException e) {
            throw exception(CLIENT, ENTITY_EXCEPTION, "USER_DISABLED " + e.getMessage());
        } catch (BadCredentialsException e) {
            throw exception(CLIENT, ENTITY_EXCEPTION, "INVALID_CREDENTIALS " + e.getMessage());
        }

        final UserDetails userDetails = jwtInMemoryUserDetailsService
                .loadUserByUsername(clientAuthDto.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);
        return new JwtResponse(token);
    }
}
