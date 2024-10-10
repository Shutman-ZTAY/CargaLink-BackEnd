package com.ipn.mx.service.implementation;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.EstatusRepTrans;
import com.ipn.mx.model.repository.EmpresaRepository;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;
import com.ipn.mx.service.interfaces.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtServiceImpl implements JwtService {
	
	private static final String SECRET_KEY = "jBfmqS0+AH16p/e8TZ7QFQPLAK8yqLBq/5j0fO6c/Oc=";
	
	@Autowired
	private EmpresaRepository er;
	@Autowired 
	private RepresentanteTransporteRepository rtr;

	@Override
	public String getToken(Usuario usuario) {
		HashMap<String, Object> claims = new HashMap<>();
		claims.put("idUsuario", usuario.getIdUsuario());
		claims.put("nombre", usuario.getNombre());
		claims.put("correo", usuario.getCorreo());
		claims.put("rol", usuario.getRol().toString());

		if (usuario instanceof RepresentanteCliente) {
			RepresentanteCliente representanteCliente = (RepresentanteCliente) usuario;
			if(representanteCliente.getEmpresaCliente() != null && 
					!er.existsById(representanteCliente.getEmpresaCliente().getRazonSocial())) {				
				claims.put("empresaRegistrada", false);
			} else {
				claims.put("empresaRegistrada", true);
			}
		} else if (usuario instanceof RepresentanteTransporte) {
			RepresentanteTransporte representanteTransporte = rtr.findById(usuario.getIdUsuario()).get();
			if(representanteTransporte.getEmpresaTransporte() != null &&
					!er.existsById(representanteTransporte.getEmpresaTransporte().getRazonSocial())) {
				claims.put("empresaRegistrada", false);
			} else {
				claims.put("empresaRegistrada", true);
			}
			if (representanteTransporte.getEstatusRepTrans() == EstatusRepTrans.VALIDO) {
				claims.put("validado", true);
			}else {
				claims.put("validado", false);
			}
		}
		return getToken(claims, usuario); 
	}
	
	private String getToken(Map<String, Object> extraClaims, Usuario usuario) {
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

	@Override
	public String getUsernameFromToken(String token) {
		return getClaim(token, Claims::getSubject);
	}

	@Override
	public boolean isTokenValid(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}
	
	private Claims getAllClaims(String token) {
		return Jwts
				.parser()
				.verifyWith(getKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
		
	}
	
	public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	private Date getExpiration(String token) {
		return getClaim(token, Claims::getExpiration);
	}
	
	private boolean isTokenExpired(String token) {
		return getExpiration(token).before(new Date()); 
	}
}
