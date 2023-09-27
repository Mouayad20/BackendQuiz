package com.mk.BackendQuiz.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.PositiveOrZero;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "sale_operation")
@Accessors(chain = true)
public class SaleOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @PositiveOrZero
    private Double total;
    @Column(name = "creation_data")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationData;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private Client client;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private Client seller;

    @OneToMany(mappedBy = "saleOperation", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private List<Transaction> transactions;

}
