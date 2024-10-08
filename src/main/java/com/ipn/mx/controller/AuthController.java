package com.ipn.mx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.AuthResponse;
import com.ipn.mx.model.dto.LoginUsuario;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.repository.UsuarioRepository;
import com.ipn.mx.service.interfaces.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody LoginUsuario usuario) {
		return ResponseEntity.ok(authService.login(usuario));
	}
	
	@PostMapping(value = "/transportista")
	public ResponseEntity<?> registerTransportista(
			@RequestBody Transportista transportista) {
		try {
			return ResponseEntity.ok(authService.registerTransportista(transportista));
		} catch (Exception e) {
			String mensajeError = "No se pudo insertar la categoría";
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
	
	@PostMapping(value = "/representante/cliente")
	public ResponseEntity<?> registerRepresentanteCliente(
			@RequestBody RepresentanteCliente representanteCliente) {
		try {
			return ResponseEntity.ok(authService.registerReprCliente(representanteCliente));
		} catch (Exception e) {
			String mensajeError = "No se pudo insertar la categoría";
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
	
	@PostMapping(value = "/representante/transporte")
	public ResponseEntity<?> registerRepresentanteTransporte(
			@RequestBody RepresentanteTransporte representanteTransporte) {
		try {
			return ResponseEntity.ok(authService.registerReprTransporte(representanteTransporte));
		} catch (Exception e) {
			String mensajeError = "No se pudo insertar la categoría";
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
}
