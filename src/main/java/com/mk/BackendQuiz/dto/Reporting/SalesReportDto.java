package com.mk.BackendQuiz.dto.Reporting;

import com.mk.BackendQuiz.dto.Client.ClientDto;
import com.mk.BackendQuiz.dto.Product.ProductFetchDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class SalesReportDto {
    private Long totalNumberOfSales;
    private Double totalRevenue;
    private List<ProductFetchDto> topSellingProducts;
    private List<ClientDto> topPerformingSellers;

    public SalesReportDto(Long totalNumberOfSales, Double totalRevenue) {
        this.totalNumberOfSales = totalNumberOfSales;
        this.totalRevenue = totalRevenue;
    }
}
