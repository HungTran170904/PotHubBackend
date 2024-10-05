package com.greb.pothubbackend.security;

import com.greb.pothubbackend.configs.JwtConfig;
import com.greb.pothubbackend.constraints.TokenType;
import com.greb.pothubbackend.models.Account;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtProvider {
	private final JwtConfig jwtConfig;

	public String generateToken(Account user, TokenType tokenType) {
		Map<String, Object> claims = new HashMap();
		claims.put("id", user.getId());
		claims.put("type", tokenType.name());

		Date currentDate = new Date();
		Date expireDate;
		if(tokenType==TokenType.ACCESS_TOKEN){
			expireDate = new Date(currentDate.getTime() + jwtConfig.accessExpiration());
			claims.put("fullname", user.getFullName());
			claims.put("role", user.getRole().name());
		}
		else { //refresh token
			expireDate = new Date(currentDate.getTime() + jwtConfig.refreshExpiration());
		}

		String token= Jwts.builder()
				.setSubject(user.getId())
				.setClaims(claims)
				.setIssuedAt(currentDate)
				.setExpiration(expireDate)
				.signWith(SignatureAlgorithm.HS512, jwtConfig.secret().getBytes())
				.compact();
		return jwtConfig.prefix()+token;
	}

	public String getIdFromToken(String bearerToken) {
		try {
			String token= bearerToken.replace(jwtConfig.prefix(),"");
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(jwtConfig.secret().getBytes())
					.build()
					.parseClaimsJws(token)
					.getBody();

			return claims.get("id").toString();
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",ex.fillInStackTrace());
		}
	}

	public Map<String, Object> getClaimsFromToken(String bearerToken) {
		try {
			String token= bearerToken.replace(jwtConfig.prefix(),"");
			Claims claims = Jwts.parserBuilder()
					.setSigningKey(jwtConfig.secret().getBytes())
					.build()
					.parseClaimsJws(token)
					.getBody();
			return claims;
		} catch (Exception ex) {
			throw new AuthenticationCredentialsNotFoundException("JWT was exprired or incorrect",ex.fillInStackTrace());
		}
	}
}
