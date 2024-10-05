package com.greb.pothubbackend.controllers;

import com.greb.pothubbackend.dtos.auth.ActiveAccountDto;
import com.greb.pothubbackend.dtos.auth.ChangePasswordDto;
import com.greb.pothubbackend.dtos.auth.ReqLoginDto;
import com.greb.pothubbackend.dtos.auth.ResLoginDto;
import com.greb.pothubbackend.dtos.account.ReqAccountDto;
import com.greb.pothubbackend.dtos.account.ResAccountDto;
import com.greb.pothubbackend.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/account/login")
    public ResponseEntity<ResLoginDto> login(
            @RequestBody @Valid ReqLoginDto reqDto
    ){
        return ResponseEntity.ok(authService.login(reqDto));
    }

    @PostMapping("/account")
    public ResponseEntity<ResAccountDto> register(
            @RequestBody @Valid ReqAccountDto reqDto
    ){
        return ResponseEntity.ok(authService.register(reqDto));
    }

    @PostMapping("/otp-code/{email}")
    public ResponseEntity<Void> sendOtpCode(
            @PathVariable String email
    ){
        authService.sendOtpCode(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/account/active")
    public ResponseEntity<Void> activateAccount(
            @RequestBody ActiveAccountDto dto
    ){
        authService.activeAccount(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/account/password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordDto dto
    ){
        authService.changePassword(dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/account/new-access-token")
    public ResponseEntity<String> getNewAccessToken(
            @RequestParam("refreshToken") String refreshToken
    ){
        return ResponseEntity.ok(authService.getNewAccessToken(refreshToken));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/account/logout")
    public ResponseEntity<String> logout(){
        authService.logout();
        return ResponseEntity.noContent().build();
    }
}
