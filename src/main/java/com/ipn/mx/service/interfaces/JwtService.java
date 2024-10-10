package com.ipn.mx.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

import com.ipn.mx.model.entity.Usuario;

public interface JwtService {

	public String getToken(Usuario usuario);
	public String getUsernameFromToken(String token);
	public boolean isTokenValid(String token, UserDetails userDetails);
	
}
