package com.github.gerdanyJr.weekit.model.req;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateStudentReq(
        @NotBlank(message = "FirstName is required") @Size(max = 50, message = "FirstName cannot exceed 50 characters") String firstName,
        @NotBlank(message = "LastName is required") @Size(max = 50, message = "LastName cannot exceed 50 characters") String lastName,
        @NotBlank(message = "cpf is required") @CPF(message = "invalid cpf") String cpf,
        @NotBlank(message = "registrationNumber is required") @Size(min = 12, max = 12, message = "RegistrationNumber should have 12 digits") String registrationNumber) {

}
