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
public class ProductDto {
    private Long id;
    private String name;
    private String category;
    private String description;
    private Double price;
    private Date creationData;
    private Long availableQuantity;
    private Long initialQuantity;
}
