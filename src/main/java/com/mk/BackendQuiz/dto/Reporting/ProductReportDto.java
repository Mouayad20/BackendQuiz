package com.mk.BackendQuiz.dto.Reporting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ProductReportDto {
    private List<InventoryStatus> inventoryStatus;
    private PriceAnalysis priceAnalysis;
}
