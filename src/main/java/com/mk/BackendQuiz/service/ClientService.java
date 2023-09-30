package com.mk.BackendQuiz.service;

import com.mk.BackendQuiz.dto.Client.ClientDto;
import com.mk.BackendQuiz.dto.Reporting.ClientReportDto;
import com.mk.BackendQuiz.exception.EntityType;
import com.mk.BackendQuiz.exception.ExceptionManager;
import com.mk.BackendQuiz.exception.ExceptionType;
import com.mk.BackendQuiz.model.Client;
import com.mk.BackendQuiz.repository.ClientRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mk.BackendQuiz.exception.EntityType.CLIENT;
import static com.mk.BackendQuiz.exception.ExceptionType.*;

@Service
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private ModelMapper modelMapper;

    public Page<ClientDto> fetchClients(Pageable pageable) {
        logger.info("fetch all clients.");
        return clientRepository.findAll(pageable).map(client -> modelMapper.map(client, ClientDto.class));

    }

    public ClientDto updateClient(ClientDto clientDto) {
        logger.info("client with email " + clientDto.getEmail() + " trying to update information. ");
        Optional<Client> client = clientRepository.findById(clientDto.getId());
        if (client.isEmpty())
            throw exception(CLIENT, ENTITY_NOT_FOUND, "");

        if (clientDto.getName() != null)
            client.get().setName(clientDto.getName());
        if (clientDto.getLastName() != null)
            client.get().setLastName(clientDto.getLastName());
        if (clientDto.getAddress() != null)
            client.get().setAddress(clientDto.getAddress());
        if (clientDto.getMobile() != null) {
            if (clientRepository.findByMobile(clientDto.getMobile()).isPresent())
                throw exception(CLIENT, DUPLICATE_ENTITY, " Mobile " + clientDto.getMobile());
            client.get().setMobile(clientDto.getMobile());
        }
        if (clientDto.getEmail() != null) {
            if (clientRepository.findByEmail(clientDto.getEmail()).isPresent())
                throw exception(CLIENT, DUPLICATE_ENTITY, " Email " + clientDto.getEmail());

            client.get().setEmail(clientDto.getEmail());

            client.get().setActivated(false);
            client.get().setActivationKey((int) Math.floor(Math.random() * (99998 - 10000 + 1) + 10000) + "");

            String subject = "Backend Quiz Verification Email";
            String text = "You have updated your email in our app And your verification code is " + client.get().getActivationKey();

            try {
                emailService.sendEmail(client.get().getEmail(), subject, text);
            } catch (MessagingException e) {
                throw exception(CLIENT, ENTITY_EXCEPTION, " Verification Email Not Sent ");
            }
        }

        return modelMapper.map(clientRepository.save(client.get()), ClientDto.class);
    }

    @Transactional
    public Boolean deleteClient(Long id) {
        logger.info("client with id " + id + " trying to delete account. ");
        Optional<Client> client = clientRepository.findById(id);
        if (client.isEmpty())
            throw exception(CLIENT, ENTITY_NOT_FOUND, "");

        clientRepository.delete(client.get());

        return !clientRepository.findById(id).isPresent();
    }

    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return ExceptionManager.throwException(entityType, exceptionType, args);
    }

    public ClientReportDto clientReport() {
        logger.info("client reporting... ");
        ClientReportDto clientReportDto = new ClientReportDto();
        clientReportDto.setTotalNumberOfClients(clientRepository.count());
        clientReportDto.setTopSpendingClients(
                clientRepository
                        .findTopSpendingClients()
                        .stream()
                        .limit(5)
                        .map(client -> modelMapper.map(client, ClientDto.class))
                        .collect(Collectors.toList())
        );
        return clientReportDto;
    }
}
