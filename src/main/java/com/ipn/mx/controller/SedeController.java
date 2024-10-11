package com.ipn.mx.controller;

import java.security.Security;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.SedeDTO;
import com.ipn.mx.model.dto.SedeId;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Sede;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;
import com.ipn.mx.model.repository.SedeRepository;

@RestController
@RequestMapping("/representante/sede")
public class SedeController {

	//TODO La aplicación web permitirá a los representantes de empresas de autotransporte podrán añadir, modificar y eliminar sus sedes.
	
	@Autowired
	private SedeRepository sedeRepository;
	@Autowired
	private RepresentanteTransporteRepository rtr;
	
	@PostMapping("")
	public ResponseEntity<?> createSede(@RequestBody Sede sede){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
				sede.setEmpresaTransporte(rt.getEmpresaTransporte());
				sedeRepository.save(sede);
				return ResponseEntity.status(HttpStatus.CREATED).body(null);
			} catch (Exception e) {
				String messageError = "Error interno en el servidor: " + e.getMessage(); 
				return ResponseEntity.status(HttpStatus.CREATED).body(messageError);
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	@GetMapping("")
	public ResponseEntity<?> viewAllSedes(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
				List<SedeDTO> ls = sedeRepository.findAllSedesByEmpresaTransporte(rt.getEmpresaTransporte().getRazonSocial());
				return ResponseEntity.status(HttpStatus.CREATED).body(ls);
			} catch (Exception e) {
				String messageError = "Error interno en el servidor: " + e.getMessage(); 
				return ResponseEntity.status(HttpStatus.CREATED).body(messageError);
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	@PutMapping("")
	public ResponseEntity<?> updateSede(@RequestBody SedeDTO sedeDTO){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Sede s;
			try {
				s = sedeRepository.findById(sedeDTO.getIdSede()).get();
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(e.getMessage());
			}
			Integer idSede = s.getIdSede();
			String newNombre = s.getNombre();
			String newDireccion = s.getDireccion();
			
			if(s.getNombre() != null)
				newNombre = s.getNombre();
			if(s.getDireccion() != null)
				newDireccion = s.getDireccion();
			
			try {
				sedeRepository.updateSede(idSede, newNombre, newDireccion);
				return ResponseEntity.status(HttpStatus.CREATED).body(null);
			} catch (Exception e) {
				String messageError = "Error interno en el servidor: " + e.getMessage(); 
				return ResponseEntity.status(HttpStatus.CREATED).body(messageError);
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	@DeleteMapping("")
	public ResponseEntity<?> deleteSede(@RequestBody SedeId sedeId){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				sedeRepository.deleteById(sedeId.getIdSede());
				return ResponseEntity.status(HttpStatus.OK).body(null);
			} catch (Exception e) {
				String messageError = "Error interno en el servidor: " + e.getMessage(); 
				return ResponseEntity.status(HttpStatus.CREATED).body(messageError);
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	
	private boolean isAuthorised(Authentication auth, RolUsuario rol) {
		Usuario u = (Usuario) auth.getPrincipal();
		if (auth == null || !auth.isAuthenticated()) {
            return false;
        }else if (u.getRol() != rol) {
        	return false;
        }else {
        	return true;
        }
	}
}
