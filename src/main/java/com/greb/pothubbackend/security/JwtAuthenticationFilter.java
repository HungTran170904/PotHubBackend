package com.greb.pothubbackend.security;

import com.greb.pothubbackend.configs.JwtConfig;
import com.greb.pothubbackend.constraints.TokenType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtConfig jwtConfig;
    private final JwtProvider jwtProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String rawToken=null;
        for(Cookie cookie : request.getCookies()) {
            if(cookie.getName().equals(TokenType.ACCESS_TOKEN.name()))
                rawToken = cookie.getValue();
        }

        if(rawToken==null || !rawToken.startsWith(jwtConfig.prefix()))
            filterChain.doFilter(request, response);

        try{
            String token=rawToken.replace(jwtConfig.prefix(), "");
            String id= jwtProvider.getIdFromToken(token);
            CustomUserDetails userDetails=userDetailsService.loadUserById(id);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                    userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            filterChain.doFilter(request, response);
        }
    }
}
