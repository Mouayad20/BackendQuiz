package com.mk.BackendQuiz.dto.Reporting;

import com.mk.BackendQuiz.dto.Client.ClientDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ClientReportDto {
    private Long totalNumberOfClients;
    private List<ClientDto> topSpendingClients;
}
