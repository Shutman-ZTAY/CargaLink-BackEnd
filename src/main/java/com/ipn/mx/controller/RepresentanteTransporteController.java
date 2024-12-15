package com.ipn.mx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.CalificacionDTO;
import com.ipn.mx.model.dto.RepTransStatus;
import com.ipn.mx.model.dto.RepresentanteTransporteCalificacion;
import com.ipn.mx.model.dto.RepresentanteTransporteSeguro;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.CalificacionRepository;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;

@RestController
@RequestMapping("")
public class RepresentanteTransporteController {
	
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private CalificacionRepository calificacionRepository;

	@GetMapping({"/representante/cliente/detalles/RepTrans/{idRepresentanteTransporte}", "/administrador/RepTrans/{idRepresentanteTransporte}"})
	public ResponseEntity<?> viewDetailsRepresentanteTransporte(@PathVariable String idRepresentanteTransporte){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				RepresentanteTransporte rt = 
						rtr.findById(idRepresentanteTransporte).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
				List<CalificacionDTO> lcdto = calificacionRepository.findAllCalificacionesByRepTransporte(idRepresentanteTransporte);
				RepresentanteTransporteCalificacion rtc = new RepresentanteTransporteCalificacion(
								RepresentanteTransporteSeguro.usuarioToUsuarioSeguro(rt),
								lcdto
							);
				return ControllerUtils.okResponse(rtc);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@PatchMapping("/administrador/RepTrans/{idRepresentanteTransporte}")
	public ResponseEntity<?> changeStatusRepTrans(
			@PathVariable String idRepresentanteTransporte, 
			@RequestBody RepTransStatus repTransStatus){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.ADMINISTRADOR)) {
			try {
				RepresentanteTransporte rt = rtr.findById(idRepresentanteTransporte).orElseThrow(() -> new NoSuchElementException("Usuario no encontrado"));
				rt.setEstatusRepTrans(repTransStatus.getEstatus());
				rtr.save(rt);
				return ControllerUtils.okResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@GetMapping("/administrador/RepTrans")
	public ResponseEntity<?> viewAllNotValidRepTrans(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.ADMINISTRADOR)) {
			try {
				List<RepresentanteTransporte> lrt = rtr.findAllNotValid();
				List<RepresentanteTransporteSeguro> lrtDto = new ArrayList<RepresentanteTransporteSeguro>(); 
				for (RepresentanteTransporte representanteTransporte : lrt) {
					lrtDto.add(RepresentanteTransporteSeguro.usuarioToUsuarioSeguro(representanteTransporte));
				}
				return ControllerUtils.okResponse(lrtDto);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@GetMapping("/administrador/RepTrans/all")
	public ResponseEntity<?> viewAllRepTrans(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.ADMINISTRADOR)) {
			try {
				List<RepresentanteTransporte> lrt = rtr.findAll();
				List<RepresentanteTransporteSeguro> lrtDto = new ArrayList<RepresentanteTransporteSeguro>(); 
				for (RepresentanteTransporte representanteTransporte : lrt) {
					lrtDto.add(RepresentanteTransporteSeguro.usuarioToUsuarioSeguro(representanteTransporte));
				}
				return ControllerUtils.okResponse(lrtDto);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
}
