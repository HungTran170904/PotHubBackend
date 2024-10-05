package com.greb.pothubbackend.dtos.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ReqAccountDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 10)
    private String password;

    @NotBlank
    private String fullName;
}
