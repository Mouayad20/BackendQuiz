package com.mk.BackendQuiz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "transaction")
@Accessors(chain = true)
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @PositiveOrZero
    private Double price;
    @PositiveOrZero
    private Long quantity;

    @ManyToOne
    @JoinColumn(name = "sale_operation_id")
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private SaleOperation saleOperation;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private Product product;

}



