package com.ipn.mx.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.SedeDTO;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Sede;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;
import com.ipn.mx.model.repository.SedeRepository;

@RestController
@RequestMapping("/representante/transporte/sede")
public class SedeController {
	
	@Autowired
	private SedeRepository sedeRepository;
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private ControllerUtils controllerUtils;
	
	@PostMapping("")
	public ResponseEntity<?> createSede(@RequestBody(required = true) Sede sede){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
				sede.setEmpresaTransporte(rt.getEmpresaTransporte());
				sedeRepository.save(sede);
				return ControllerUtils.createdResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	@GetMapping("")
	public ResponseEntity<?> viewAllSedes(@RequestParam(required = false) String idRepresentanteTrans){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			RepresentanteTransporte rt;
			try {
				if (idRepresentanteTrans != null)
					rt = rtr.findById(idRepresentanteTrans).get();
				else
					rt = rtr.findById(u.getIdUsuario()).get();
				
				List<SedeDTO> ls = sedeRepository.findAllSedesByEmpresaTransporte(rt.getEmpresaTransporte().getRazonSocial());
				return ControllerUtils.okResponse(ls);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> viewSede(@PathVariable Integer id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				Sede s = sedeRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Sede no encontrada"));
				if(!controllerUtils.perteneceAlUsuario(u, s)) {
					return ControllerUtils.unauthorisedResponse();
				};
				return ControllerUtils.okResponse(SedeDTO.toSedeDTO(s));
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateSede(
			@PathVariable Integer id,
			@RequestBody(required = true) SedeDTO sedeUpd){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Sede s = sedeRepository.findById(id).get();;
			if (controllerUtils.perteneceAlUsuario(u, s)) {
				Integer idSede = s.getIdSede();
				String newNombre = s.getNombre();
				String newDireccion = s.getDireccion();
				
				if(sedeUpd.getNombre() != null)
					newNombre = sedeUpd.getNombre();
				if(sedeUpd.getDireccion() != null)
					newDireccion = sedeUpd.getDireccion();
				
				try {
					sedeRepository.updateSede(idSede, newNombre, newDireccion);
					return ControllerUtils.okResponse();
				} catch (Exception e) {
					return ControllerUtils.exeptionsResponse(e);
				}
			} else 
				return ControllerUtils.badRequestResponse();
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	@DeleteMapping("/{idSede}")
	public ResponseEntity<?> deleteSede(@PathVariable Integer idSede){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				if (controllerUtils.perteneceAlUsuario((Usuario) auth.getPrincipal(), sedeRepository.findById(idSede).get())) {
						sedeRepository.deleteById(idSede);
						return ControllerUtils.okResponse();
				} else 
					return ControllerUtils.badRequestResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
}
