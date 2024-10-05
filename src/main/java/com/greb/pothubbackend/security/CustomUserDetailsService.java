package com.greb.pothubbackend.security;

import com.greb.pothubbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public CustomUserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user=userRepository.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("Email "+email+" not found"));
        return new CustomUserDetails(user);
    }

    public CustomUserDetails loadUserById(String id) {
        var user=userRepository.findById(id).orElseThrow(()->new UsernameNotFoundException("UserId "+id+" not found"));
        return new CustomUserDetails(user);
    }
}
