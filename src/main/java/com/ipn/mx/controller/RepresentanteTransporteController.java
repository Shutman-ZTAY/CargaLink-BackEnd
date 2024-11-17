package com.ipn.mx.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.CalificacionDTO;
import com.ipn.mx.model.dto.RepresentanteTransporteCalificacion;
import com.ipn.mx.model.dto.RepresentanteTransporteSeguro;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.CalificacionRepository;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;

@RestController
@RequestMapping("/representante")
public class RepresentanteTransporteController {
	
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private CalificacionRepository calificacionRepository;

	@GetMapping("/cliente/detalles/RepTrans/{idRepresentanteTransporte}")
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
}
