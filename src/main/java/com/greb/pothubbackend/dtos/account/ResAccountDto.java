package com.greb.pothubbackend.dtos.account;

import com.greb.pothubbackend.constraints.UserRole;
import com.greb.pothubbackend.models.Account;
import lombok.Data;

@Data
public class ResAccountDto {
    private String id;

    private String email;

    private String fullName;

    private UserRole role;

    public static ResAccountDto fromAccount(Account account) {
        ResAccountDto dto = new ResAccountDto();
        dto.setId(account.getId());
        dto.setEmail(account.getEmail());
        dto.setFullName(account.getFullName());
        dto.setRole(account.getRole());
        return dto;
    }
}
