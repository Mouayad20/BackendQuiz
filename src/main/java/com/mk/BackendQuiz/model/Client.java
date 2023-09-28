package com.mk.BackendQuiz.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "client")
@Accessors(chain = true)
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @Column(name = "last_name")
    private String lastName;
    private String address;
    @Column(unique = true)
    private String mobile;

    @NotNull
    @Column(nullable = false)
    private boolean activated = false;
    @Size(max = 20)
    @Column(name = "activation_key", length = 20)
    @JsonIgnore
    private String activationKey;

    @Email
    @NotNull
    @Column(unique = true)
    private String email;

    @JsonIgnore
    @NotNull
    @Size(min = 10, max = 60)
    @Column(name = "password_hash", length = 60, nullable = false)
    private String password;

    @OneToMany(mappedBy = "publisher", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private List<Product> products;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private List<SaleOperation> salesOperations;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = {}, allowSetters = true)
    private List<SaleOperation> purchaseOperations;

}
