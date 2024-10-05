package com.greb.pothubbackend.security;

import com.greb.pothubbackend.exceptions.UnauthorizedException;
import com.greb.pothubbackend.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepo;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var account=accountRepo.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Email "+email+" not found"));
        return new CustomUserDetails(account);
    }

    public CustomUserDetails loadUserById(String id) {
        var account=accountRepo.findById(id).orElseThrow(()->new UnauthorizedException("AccountId "+id+" not found"));
        return new CustomUserDetails(account);
    }
}
