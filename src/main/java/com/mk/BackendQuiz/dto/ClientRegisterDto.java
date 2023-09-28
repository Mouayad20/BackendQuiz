package com.mk.BackendQuiz.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class ClientRegisterDto {
    private String name;
    private String lastName;
    private String address;
    private String mobile;
    private String email;
}