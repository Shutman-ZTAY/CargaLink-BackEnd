package com.ipn.mx.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ipn.mx.model.dto.LoginUsuario;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.enumerated.EstatusRepTrans;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.service.interfaces.AuthService;
import com.ipn.mx.service.interfaces.FilesService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	@Autowired
	private FilesService filesService;
	
	//RF01	Iniciar sesión
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
	
	//RF02 Registro
	@PostMapping(value = "/representante/cliente", consumes = { "application/octet-stream", "multipart/form-data" })
	public ResponseEntity<?> registerRepresentanteCliente(
			@RequestPart("representanteCliente") RepresentanteCliente representanteCliente,
			@RequestPart("image") MultipartFile imagen) {
		
		representanteCliente.setRol(RolUsuario.REPRESENTANTE_CLIENTE);
		String filename = null;
		if(!imagen.isEmpty() && representanteCliente.getEmpresaCliente() != null) {
			try {
				filename = filesService.saveImage(imagen);
				representanteCliente.getEmpresaCliente().setLogo(filename);
			} catch (Exception e) {
				if (filename != null)
					filesService.delete(filename);
				return ControllerUtils.exeptionsResponse(new RuntimeException("No se pudo guardar los archivos"));
			}
		}
		try {
			return ResponseEntity.ok(authService.registerReprCliente(representanteCliente));
		} catch (Exception e) {
			if (filename != null)
				filesService.delete(filename);
			String mensajeError = "Error interno en el servidor: " + e.getMessage();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
	
	//RF02 Registro
	@PostMapping(value = "/representante/transporte", consumes = { "application/octet-stream", "multipart/form-data" })
	public ResponseEntity<?> registerRepresentanteTransporte(
			@RequestPart("representanteTransporte") RepresentanteTransporte representanteTransporte,
			@RequestPart("file") MultipartFile file,
	        @RequestPart("image") MultipartFile imagen) {
		
		representanteTransporte.setRol(RolUsuario.REPRESENTANTE_TRANSPORTE);
		representanteTransporte.setEstatusRepTrans(EstatusRepTrans.NO_VALIDO);
		String imagename = null;
		String filename = null;
		if(!file.isEmpty() && !imagen.isEmpty() && representanteTransporte.getEmpresaTransporte() != null) {
			try {
				imagename = filesService.saveImage(imagen);
				filename = filesService.savePdf(file);
				representanteTransporte.getEmpresaTransporte().setLogo(imagename);
				representanteTransporte.getEmpresaTransporte().setDocumentoFiscal(filename);	
			} catch (Exception e) {
				if (imagename != null)
					filesService.delete(imagename);
				if (filename != null)
					filesService.delete(filename);
				return ControllerUtils.exeptionsResponse(new RuntimeException("No se pudo guardar los archivos"));
			}
		}
		try {
			return ResponseEntity.ok(authService.registerReprTransporte(representanteTransporte));
		} catch (Exception e) {
			String mensajeError = "Error interno en el servidor: " + e.getMessage();
			if (imagename != null)
				filesService.delete(imagename);
			if (filename != null)
				filesService.delete(filename);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
		}
	}
}
