package com.mk.BackendQuiz.dto.Reporting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class PriceAnalysis {
    private Double minPrice;
    private Double maxPrice;
    private Double avgPrice;

    public PriceAnalysis(Double minPrice, Double maxPrice, Double avgPrice) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.avgPrice = avgPrice;
    }
}
