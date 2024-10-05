package com.greb.pothubbackend.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ChangePasswordDto {
    @Pattern(regexp= "^\\d{6}$")
    private String otpCode;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Length(min = 10)
    private String newPassword;
}
