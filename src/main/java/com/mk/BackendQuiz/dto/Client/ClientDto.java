package com.mk.BackendQuiz.dto.Client;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ClientDto {
    private Long id;
    private String name;
    private String lastName;
    private String address;
    private String mobile;
    private String email;

    public ClientDto(
            Long id,
            String name,
            String lastName,
            String address,
            String mobile,
            String email
    ) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.address = address;
        this.mobile = mobile;
        this.email = email;
    }
}