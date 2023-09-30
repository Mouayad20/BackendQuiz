package com.mk.BackendQuiz.controller;

import com.mk.BackendQuiz.dto.Sales.SaleOperationDto;
import com.mk.BackendQuiz.dto.Sales.SaleOperationFetchDto;
import com.mk.BackendQuiz.dto.Sales.TransactionDto;
import com.mk.BackendQuiz.dto.response.Response;
import com.mk.BackendQuiz.service.SaleOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/saleOperations")
public class SaleOperationController {
    @Autowired
    SaleOperationService saleOperationService;

    @GetMapping(value = "/fetch")
    public Response fetchSaleOperations(Pageable pageable) {
        Page<SaleOperationFetchDto> page = saleOperationService.fetchSaleOperations(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return Response.ok().setPayload(page.getContent()).setMetadata(headers);
    }

    @PostMapping(value = "/create")
    public Response createSaleOperation(
            @RequestBody @Valid List<TransactionDto> transactions,
            @RequestHeader("Authorization") String token
    ) {
        return Response.ok().setPayload(saleOperationService.createSaleOperation(transactions, token));
    }

    @PatchMapping(value = "/update")
    public Response updateSaleOperation(@RequestBody SaleOperationDto saleOperationDto) {
        return Response.ok().setPayload(saleOperationService.updateSaleOperation(saleOperationDto));
    }

    @GetMapping(value = "/report")
    public Response saleReport(
            @RequestParam("from") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date from,
            @RequestParam("to") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date to) {
        return Response.ok().setPayload(saleOperationService.saleReport(from, to));
    }

}
