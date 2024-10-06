package com.ipn.mx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.AuthResponse;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.service.interfaces.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;

	@PostMapping(value = "/login")
	public ResponseEntity<AuthResponse> login(@RequestBody Usuario usuario) {
		//TODO Implementar login para todos los usuarios
		return ResponseEntity.ok(new AuthResponse());
	}
	
	@PostMapping(value = "/transportista")
	public ResponseEntity<AuthResponse> registerTransportista(
			@RequestBody Transportista transportista) {
		return ResponseEntity.ok(authService.registerTransportista(transportista));
	}
	
	@PostMapping(value = "/representante/cliente")
	public ResponseEntity<AuthResponse> registerRepresentanteCliente(
			@RequestBody RepresentanteCliente representanteCliente) {
		//TODO Implementar registrar para este tipo de usuario
		return ResponseEntity.ok(new AuthResponse());
	}
	
	@PostMapping(value = "representante/transporte")
	public ResponseEntity<AuthResponse> registerRepresentanteTransporte(
			@RequestBody RepresentanteTransporte representanteTransporte) {
		//TODO Implementar registrar para este tipo de usuario
		return ResponseEntity.ok(new AuthResponse());
	}
}
