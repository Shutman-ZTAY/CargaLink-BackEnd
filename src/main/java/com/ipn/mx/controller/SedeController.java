package com.ipn.mx.controller;

import java.util.List;
import java.util.Optional;

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
				System.out.println("");
				System.out.println(idRepresentanteTrans);
				System.out.println("");
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
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateSede(
			@PathVariable Integer id,
			@RequestBody(required = true) SedeDTO sedeUpd){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Sede s = sedeRepository.findById(id).get();;
			if (perteneceAlUsuario(u, s)) {
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
				if (perteneceAlUsuario((Usuario) auth.getPrincipal(), sedeRepository.findById(idSede).get())) {
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
	
	private boolean perteneceAlUsuario(Usuario u, Sede sede){
		if (sede == null) {
			return false;
		}
		if (u.getRol() == RolUsuario.ADMINISTRADOR) {
			RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
			Optional<SedeDTO> os = sedeRepository.findSedeByEmpresaAndId(sede.getIdSede(), rt.getEmpresaTransporte().getRazonSocial());
			if(!os.isEmpty())
				return true;
			else
				return false;
		} else {
			return true;
		}
	}

}
