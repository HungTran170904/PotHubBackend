package com.greb.pothubbackend.services;

import com.greb.pothubbackend.constraints.TokenType;
import com.greb.pothubbackend.constraints.UserRole;
import com.greb.pothubbackend.dtos.auth.ActiveAccountDto;
import com.greb.pothubbackend.dtos.auth.ChangePasswordDto;
import com.greb.pothubbackend.dtos.auth.ReqLoginDto;
import com.greb.pothubbackend.dtos.auth.ResLoginDto;
import com.greb.pothubbackend.dtos.account.ReqAccountDto;
import com.greb.pothubbackend.dtos.account.ResAccountDto;
import com.greb.pothubbackend.exceptions.BadRequestException;
import com.greb.pothubbackend.exceptions.UnauthorizedException;
import com.greb.pothubbackend.models.Account;
import com.greb.pothubbackend.repositories.AccountRepository;
import com.greb.pothubbackend.security.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final AccountRepository accountRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final EmailService emailService;
    private SecureRandom secureRandom = new SecureRandom();

    public ResLoginDto login(ReqLoginDto reqDto) {
        var account= accountRepo.findByEmail(reqDto.getEmail()).orElseThrow(()-> new UnauthorizedException("Password or Email is incorrect"));
        if(!passwordEncoder.matches(reqDto.getPassword(), account.getPassword()))
            throw new UnauthorizedException("Password or Email is incorrect");

        var refreshToken= jwtProvider.generateToken(account, TokenType.REFRESH_TOKEN);
        var accessToken= jwtProvider.generateToken(account, TokenType.ACCESS_TOKEN);

        account.setRefreshToken(refreshToken);
        accountRepo.save(account);

        return ResLoginDto.from(account,accessToken,refreshToken);
    }

    public ResAccountDto register(ReqAccountDto reqDto){
        if(accountRepo.existsByEmail(reqDto.getEmail()))
            throw new BadRequestException("Email"+reqDto.getEmail()+" is already in use");

        var account= Account.builder()
                 .email(reqDto.getEmail())
                 .fullName(reqDto.getFullName())
                 .password(passwordEncoder.encode(reqDto.getPassword()))
                 .role(UserRole.USER)
                 .isEnabled(false)
                 .build();

        sendOtpCode(account);

        var savedUser= accountRepo.save(account);
        return ResAccountDto.fromAccount(savedUser);
    }

    private String generateOtpCode(){
        StringBuilder digits = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int digit = secureRandom.nextInt(10);
            digits.append(digit);
        }
        return digits.toString();
    }

    private void sendOtpCode(Account account){
        account.setOtpCode(generateOtpCode());
        account.setOtpTime(LocalDateTime.now().plusMinutes(5));

        emailService.sendMail(account.getEmail(),
                "[PotHub] Email OTP Code",
                "<div>Hi <b>"+account.getFullName() +
                        "</b>, your OTP code is <b>"+account.getOtpCode()+"</b>.</div>"+
                        "<div>Please enter it within 5 minutes</div>"
                );
    }

    public void sendOtpCode(String email){
        var account= accountRepo.findByEmail(email).orElseThrow(()-> new BadRequestException("Email not found"));
        sendOtpCode(account);
        accountRepo.save(account);
    }

    public void activeAccount(ActiveAccountDto dto){
        var account= accountRepo.findByEmail(dto.getEmail()).orElseThrow(()-> new BadRequestException("Email not found"));
        if(account.getOtpTime()==null||account.getOtpTime().isBefore(LocalDateTime.now()))
            throw new UnauthorizedException("The otp code has been expired! Please enter new OTP");
        if(account.getOtpCode()==null||!account.getOtpCode().equals(dto.getOtpCode()))
            throw new UnauthorizedException("OTP code is incorrect");

        account.setIsEnabled(true);
        account.setOtpCode(null);
        account.setOtpTime(null);

        accountRepo.save(account);
    }

    public void changePassword(ChangePasswordDto dto){
        var account= accountRepo.findByEmail(dto.getEmail()).orElseThrow(()-> new BadRequestException("Email not found"));
        if(account.getOtpTime()==null||account.getOtpTime().isBefore(LocalDateTime.now()))
            throw new UnauthorizedException("The otp code has been expired! Please enter new OTP");
        if(account.getOtpCode()==null||!account.getOtpCode().equals(dto.getOtpCode()))
            throw new UnauthorizedException("OTP code is incorrect");

        account.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        account.setOtpCode(null);
        account.setOtpTime(null);

        accountRepo.save(account);
    }

    public String getNewAccessToken(String refreshToken){
        try{
            String id = jwtProvider.getIdFromToken(refreshToken);

            var account= accountRepo.findById(id)
                    .orElseThrow(()-> new UnauthorizedException("Refresh token is invalid"));
            if(account.getRefreshToken()==null||!account.getRefreshToken().equals(refreshToken))
                throw new UnauthorizedException("Refresh token is invalid");

            String newAccessToken= jwtProvider.generateToken(account, TokenType.ACCESS_TOKEN);
            return newAccessToken;
        }
        catch(Exception ex){
            throw new UnauthorizedException("Refresh token is invalid");
        }
    }

    public void logout(){
        Authentication auth= (Authentication) SecurityContextHolder.getContext().getAuthentication();
        String accountId= auth.getName();

        var account= accountRepo.findById(accountId)
                .orElseThrow(()-> new UnauthorizedException("Account not found"));
        account.setRefreshToken(null);
        accountRepo.save(account);
    }
}
