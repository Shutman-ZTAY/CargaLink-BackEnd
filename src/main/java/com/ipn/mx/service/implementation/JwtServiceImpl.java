package com.ipn.mx.service.implementation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ipn.mx.service.interfaces.JwtService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
	
	private static final String SECRET_KEY = "jBfmqS0+AH16p/e8TZ7QFQPLAK8yqLBq/5j0fO6c/Oc=";

	@Override
	public String getToken(UserDetails usuario) {
		return getToken(new HashMap<>(), usuario); 
	}
	
	private String getToken(Map<String, Object> extraClaims, UserDetails usuario) {
		return Jwts.builder()
				.claims(extraClaims)
				.subject(usuario.getUsername())
				.issuedAt(new Date(System.currentTimeMillis()))
				.expiration(new Date(System.currentTimeMillis()+1000*60*24))	//Los tokens expiran un dia despues
				.signWith(getKey())
				.compact();
	}
	
	private SecretKey getKey(){
		byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
		return Keys.hmacShaKeyFor(keyBytes);
	}
}
