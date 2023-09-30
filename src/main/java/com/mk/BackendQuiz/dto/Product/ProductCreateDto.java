package com.mk.BackendQuiz.dto.Product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ProductCreateDto {
    private String name;
    private String category;
    private String description;
    private Double price;
    private Long initialQuantity;
}


