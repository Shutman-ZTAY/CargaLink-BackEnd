package com.ipn.mx.service.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipn.mx.model.dto.AuthResponse;
import com.ipn.mx.model.dto.LoginUsuario;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.repository.SedeRepository;
import com.ipn.mx.service.interfaces.AuthService;
import com.ipn.mx.service.interfaces.JwtService;

@Service
public class AuthServiceImpl implements AuthService {
	
	//TODO Cuando se cree el servicio correspondiente a las Sedes cambiar esto
	@Autowired
	private SedeRepository sr;
	@Autowired
	private JwtService js;

	@Override
	public AuthResponse login(LoginUsuario loginUsuario) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthResponse registerTransportista(Transportista transportista) {
		if(transportista.getSede() != null) {
			sr.save(transportista.getSede());
		}
		return AuthResponse.builder()
				.token(js.getToken(transportista))
				.build();
	}

	@Override
	public AuthResponse registerReprTransporte(RepresentanteTransporte transportista) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AuthResponse registerReprCliente(RepresentanteCliente transportista) {
		// TODO Auto-generated method stub
		return null;
	}

}
