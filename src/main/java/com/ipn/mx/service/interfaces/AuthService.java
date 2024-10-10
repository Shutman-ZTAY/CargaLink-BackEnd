package com.ipn.mx.service.interfaces;

import com.ipn.mx.model.dto.AuthResponse;
import com.ipn.mx.model.dto.LoginUsuario;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Transportista;

public interface AuthService {
	
	AuthResponse login(LoginUsuario loginUsuario);
	AuthResponse registerTransportista(Transportista transportista);
	AuthResponse registerReprTransporte(RepresentanteTransporte representanteTransporte);
	AuthResponse registerReprCliente(RepresentanteCliente representanteCliente);
	
}
