package com.mk.BackendQuiz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "product")
@Accessors(chain = true)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private String name;
    private String category;
    private String description;
    @PositiveOrZero
    private Double price;

    @Column(name = "creation_data")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationData;
    @PositiveOrZero
    @Column(name = "available_quantity")
    private Long availableQuantity;
    @PositiveOrZero
    @Column(name = "initial_quantity")
    private Long initialQuantity;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private Client publisher;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private List<Transaction> transactions;

}
