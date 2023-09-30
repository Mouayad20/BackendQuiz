package com.mk.BackendQuiz.dto.Sales;

import com.mk.BackendQuiz.dto.Product.ProductFetchDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class TransactionDto {
    private Long id;
    private Double price;
    private Long quantity;
    private ProductFetchDto product;
}
