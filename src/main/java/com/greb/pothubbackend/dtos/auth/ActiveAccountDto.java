package com.greb.pothubbackend.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ActiveAccountDto {
    @Email
    private String email;

    @Pattern(regexp= "^\\d{6}$")
    private String otpCode;
}
