package com.mk.BackendQuiz.dto.Sales;

import com.mk.BackendQuiz.dto.Client.ClientDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class SaleOperationDto {
    private Long id;
    private Double total;
    private Date creationData;
    private ClientDto client;
    private ClientDto seller;
    private List<TransactionDto> transactions;
}
