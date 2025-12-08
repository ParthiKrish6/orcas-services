package com.orcas.utils;

import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {

	private final String SECRET = "D8D29787A991A3851BF184442B1D1D8D29787A991A3851BF184442B1D1";

	public String generateToken(String username, String role) {
		return Jwts.builder().setSubject(username).claim("role", role).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1 * 60 * 1000)) // 30 min expiry
				.signWith(SignatureAlgorithm.HS256, SECRET).compact();
	}

	public Claims extractClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
	}

	public boolean isExpired(String token) {
		return extractClaims(token).getExpiration().before(new Date());
	}

	public String extractRole(String token) {
		return extractClaims(token).get("role", String.class);
	}
}