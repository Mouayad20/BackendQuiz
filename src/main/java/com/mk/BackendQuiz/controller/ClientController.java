package com.mk.BackendQuiz.controller;

import com.mk.BackendQuiz.dto.Client.ClientDto;
import com.mk.BackendQuiz.dto.response.Response;
import com.mk.BackendQuiz.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api/clients")
public class ClientController {
    @Autowired
    ClientService clientService;

    @GetMapping(value = "/fetch")
    public Response fetchClients(Pageable pageable) {
        Page<ClientDto> page = clientService.fetchClients(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return Response.ok().setPayload(page.getContent()).setMetadata(headers);
    }

    @PatchMapping(value = "/update")
    public Response updateClient(@RequestBody ClientDto clientDto) {
        return Response.ok().setPayload(clientService.updateClient(clientDto));
    }

    @DeleteMapping(value = "/delete")
    public Response deleteClient(@RequestParam(name = "id") Long id) {
        return Response.ok().setPayload(clientService.deleteClient(id));
    }
}
