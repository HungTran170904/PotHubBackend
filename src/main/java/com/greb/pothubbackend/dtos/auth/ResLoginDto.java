package com.greb.pothubbackend.dtos.auth;

import com.greb.pothubbackend.dtos.account.ResAccountDto;
import com.greb.pothubbackend.models.Account;
import lombok.Data;

@Data
public class ResLoginDto {
    private String accessToken;

    private String refreshToken;

    private ResAccountDto account;

    public static ResLoginDto from(Account account, String accessToken, String refreshToken) {
        ResLoginDto dto = new ResLoginDto();
        dto.setAccount(ResAccountDto.fromAccount(account));
        dto.setAccessToken(accessToken);
        dto.setRefreshToken(refreshToken);
        return dto;
    }
}
