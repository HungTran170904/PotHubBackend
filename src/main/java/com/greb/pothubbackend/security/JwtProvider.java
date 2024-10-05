package com.greb.pothubbackend.security;

import com.greb.pothubbackend.configs.JwtConfig;
import com.greb.pothubbackend.constraints.TokenType;
import com.greb.pothubbackend.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	private final JwtConfig jwtConfig;

	public String generateToken(Authentication auth, TokenType tokenType) {
		CustomUserDetails userDetails=(CustomUserDetails) auth.getPrincipal();
		User user= userDetails.getUser();

		Date currentDate = new Date();
		Date expireDate;
		if(tokenType==TokenType.ACCESS_TOKEN)
			expireDate = new Date(currentDate.getTime() + jwtConfig.accessExpiration());
		else //refresh token
			expireDate = new Date(currentDate.getTime() + jwtConfig.refreshExpiration());

		String token= Jwts.builder()
				.setSubject(user.getId())
				.setClaims(Map.of(
						"fullname", user.getFullName(),
						"role", user.getRole().name()))
				.setIssuedAt(currentDate)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, jwtConfig.secret().getBytes())
				.compact();
		return jwtConfig.prefix()+token;
	}

	public String getIdFromToken(String token) {
		try {
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(jwtConfig.secret().getBytes())
					.build()
					.parseClaimsJws(token)
					.getBody();
			return claims.getSubject();
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",ex.fillInStackTrace());
		}
	}
}
