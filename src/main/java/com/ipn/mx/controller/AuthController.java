package com.ipn.mx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.LoginUsuario;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.enumerated.EstatusRepTrans;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.service.interfaces.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	
	@PostMapping(value = "/login")
	public ResponseEntity<?> login(@RequestBody LoginUsuario usuario) {
		try {
			return ResponseEntity.ok(authService.login(usuario));
		}
		catch (Exception e) {
			String mensajeError = "Error interno en el servidor: " + e.getMessage();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
	
	/*
	@PostMapping(value = "/transportista")
	public ResponseEntity<?> registerTransportista(
			@RequestBody Transportista transportista) {
		transportista.setRol(RolUsuario.TRANSPORTISTA);
		try {
			AuthResponse ar = authService.registerTransportista(transportista);
			if (ar != null) {
				return ResponseEntity.status(HttpStatus.CREATED).body(ar);
			} else {
				String mensajeError = "Transportista incompleto, se debe asociar a una sede";
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensajeError);
			}
		} catch (Exception e) {
			String mensajeError = "Error interno en el servidor";
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
	*/
	
	@PostMapping(value = "/representante/cliente")
	public ResponseEntity<?> registerRepresentanteCliente(
			@RequestBody RepresentanteCliente representanteCliente) {
		representanteCliente.setRol(RolUsuario.REPRESENTANTE_CLIENTE);
		try {
			return ResponseEntity.ok(authService.registerReprCliente(representanteCliente));
		} catch (Exception e) {
			String mensajeError = "Error interno en el servidor: " + e.getMessage();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
	
	@PostMapping(value = "/representante/transporte")
	public ResponseEntity<?> registerRepresentanteTransporte(
			@RequestBody RepresentanteTransporte representanteTransporte) {
		representanteTransporte.setRol(RolUsuario.REPRESENTANTE_TRANSPORTE);
		representanteTransporte.setEstatusRepTrans(EstatusRepTrans.NO_VALIDO);
		try {
			return ResponseEntity.ok(authService.registerReprTransporte(representanteTransporte));
		} catch (Exception e) {
			String mensajeError = "Error interno en el servidor: " + e.getMessage();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
}
