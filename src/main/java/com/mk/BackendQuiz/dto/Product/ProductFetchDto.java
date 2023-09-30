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
public class ProductFetchDto {
    private Long id;
    private String name;
    private String category;
    private String description;
    private Double price;
    private Date creationData;
    private Long availableQuantity;

    public ProductFetchDto(
            Long id,
            String name,
            String category,
            String description,
            Double price,
            Date creationData,
            Long availableQuantity
    ) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.description = description;
        this.price = price;
        this.creationData = creationData;
        this.availableQuantity = availableQuantity;
    }
}
