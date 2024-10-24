package com.ipn.mx.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.PostulacionDTO;
import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.entity.Postulacion;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.EstatusOferta;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.OfertaRepository;
import com.ipn.mx.model.repository.PostulacionRepository;

@RestController
@RequestMapping("")
public class PostulacionController {
	
	@Autowired
	private PostulacionRepository postulacionRepository;
	@Autowired
	private OfertaRepository ofertaRepository;
	@Autowired
	private ControllerUtils controllerUtils;

	@PostMapping("/transporte/postulacion")
	public ResponseEntity<?> crearPostulacion(@RequestBody(required = true) Postulacion postulacion){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				postulacion.setIdPostulacion(null);
				postulacion.setRepresentanteTransporte((RepresentanteTransporte) u);
				postulacionRepository.save(postulacion);
				return ControllerUtils.createdResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@GetMapping("/transporte/postulacion")
	public ResponseEntity<?> viewAllPostulacionByRepresentanteTransporte(
			@RequestBody(required = false) String idRepresentanteTransporte){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				List<PostulacionDTO> lp;
				if (u.getRol() == RolUsuario.ADMINISTRADOR) 
					lp = postulacionRepository.findAllPostulacionesByReprTransporte(idRepresentanteTransporte);
				else
					lp = postulacionRepository.findAllPostulacionesByReprTransporte(u.getIdUsuario());
				
				return ControllerUtils.okResponse(lp);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@DeleteMapping("/transporte/postulacion/{idPostulacion}")
	public ResponseEntity<?> deletePostulacion(
			@PathVariable Integer idPostulacion){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				Postulacion p = postulacionRepository
						.findById(idPostulacion)
						.orElseThrow(
								() -> new NoSuchElementException("Elemento no encontrado"));
				if (controllerUtils.perteneceAlUsuario(u, p)) {
					postulacionRepository.deleteById(idPostulacion);
				}				
				return ControllerUtils.okResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@GetMapping("/cliente/postulacion/{idOferta}")
	public ResponseEntity<?> viewAllPostulacionByOferta(
			@PathVariable Integer idOferta){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				Oferta o = ofertaRepository.findById(idOferta).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
				if(controllerUtils.perteneceAlUsuario(u, o))
					return ControllerUtils.okResponse(o.getPostulaciones());
				else
					return ControllerUtils.unauthorisedResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@PatchMapping("/cliente/postulacion")
	public ResponseEntity<?> aceptarPostulacion(
			@RequestBody Postulacion postulacion){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				if (controllerUtils.perteneceAlUsuario(u, postulacion.getOferta())) {
					postulacionRepository.deletePostulacionesNoAceptadas(
							postulacion.getOferta().getIdOferta(),
							postulacion.getRepresentanteTransporte().getIdUsuario());
					ofertaRepository.updateEstatusOferta(
							postulacion.getOferta().getIdOferta(),
							EstatusOferta.CONFIGURANDO.name());
					return ControllerUtils.okResponse();
				} else {
					return ControllerUtils.unauthorisedResponse();
				}
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
}
