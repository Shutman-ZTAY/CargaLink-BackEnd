package com.ipn.mx.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ipn.mx.model.dto.AuthResponse;
import com.ipn.mx.model.dto.LoginUsuario;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.repository.EmpresaRepository;
import com.ipn.mx.model.repository.SedeRepository;
import com.ipn.mx.model.repository.UsuarioRepository;
import com.ipn.mx.service.interfaces.AuthService;
import com.ipn.mx.service.interfaces.JwtService;

@Service
public class AuthServiceImpl implements AuthService {
	
	@Autowired
	private SedeRepository sr;
	@Autowired
	private UsuarioRepository ur;
	@Autowired
	private EmpresaRepository er;
	@Autowired
	private JwtService js;
	@Autowired
	private AuthenticationManager am;
	@Autowired
	private PasswordEncoder pe;

	@Override
	public AuthResponse login(LoginUsuario loginUsuario) {
		am.authenticate(
				new UsernamePasswordAuthenticationToken(
						loginUsuario.getCorreo(), loginUsuario.getPassword()
						)
				);	   
		UserDetails ud = ur.findUsuarioByCorreo(loginUsuario.getCorreo()).
				orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
		String token = js.getToken(ud);
		return AuthResponse.builder()
				.token(token)
				.build();
	}

	@Override
	public AuthResponse registerTransportista(Transportista transportista) {
		if(transportista.getSede() != null) {
			if (!sr.existsById(transportista.getSede().getIdSede())) {
				sr.save(transportista.getSede());
			}
		}
		transportista.setPassword(pe.encode(transportista.getPassword()));
		ur.save(transportista);
		return AuthResponse.builder()
				.token(js.getToken(transportista))
				.build();
	}

	@Override
	public AuthResponse registerReprTransporte(RepresentanteTransporte representanteTransporte) {
		if(representanteTransporte.getEmpresaTransporte() != null) {
			if (!er.existsById(representanteTransporte.getEmpresaTransporte().getRazonSocial())) {
				er.save(representanteTransporte.getEmpresaTransporte());
			}
		}
		representanteTransporte.setPassword(pe.encode(representanteTransporte.getPassword()));
		ur.save(representanteTransporte);
		return AuthResponse.builder()
				.token(js.getToken(representanteTransporte))
				.build();
	}

	@Override
	public AuthResponse registerReprCliente(RepresentanteCliente representanteCliente) {
		if(representanteCliente.getEmpresaCliente() != null) {
			if (!er.existsById(representanteCliente.getEmpresaCliente().getRazonSocial())) {
				er.save(representanteCliente.getEmpresaCliente());
			}
		}
		representanteCliente.setPassword(pe.encode(representanteCliente.getPassword()));
		ur.save(representanteCliente);
		return AuthResponse.builder()
				.token(js.getToken(representanteCliente))
				.build();
	}

}
