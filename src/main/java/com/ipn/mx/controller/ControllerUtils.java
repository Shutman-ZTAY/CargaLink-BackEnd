package com.ipn.mx.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;

public class ControllerUtils {
	
	public static ResponseEntity<?> exeptionsResponse(Exception e){
		String messageError = "Error interno en el servidor: " + e.getMessage();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageError);
	}
	
	public static ResponseEntity<?> okResponse(Object obj){
		return ResponseEntity.status(HttpStatus.OK).body(obj);
	}
	
	public static ResponseEntity<?> okResponse(){
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	public static ResponseEntity<?> badGatewayResponse(){
		return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("No se puede accceder al recurso");
	}

	public static ResponseEntity<?> unauthorisedResponse() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
	
	public static ResponseEntity<?> createdResponse() {
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	public static boolean isAuthorised(Authentication auth, RolUsuario rol) {
		Usuario u = (Usuario) auth.getPrincipal();
		if (auth == null || !auth.isAuthenticated()) {
            return false;
        }else if (u.getRol() != rol) {
        	return false;
        }else {
        	return true;
        }
	}

	public static ResponseEntity<?> badGatewayResponse(String mensaje) {
		return ResponseEntity.status(HttpStatus.CREATED).body(mensaje);
	}
	
}
