package com.ipn.mx.service.interfaces;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

	public String getToken(UserDetails usuario);
}
