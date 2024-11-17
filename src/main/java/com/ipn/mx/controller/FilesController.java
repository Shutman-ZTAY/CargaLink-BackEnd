package com.ipn.mx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.entity.EmpresaAutotransporte;
import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.EmpresaAutotransporteRepository;
import com.ipn.mx.model.repository.OfertaRepository;
import com.ipn.mx.service.interfaces.FilesService;

@RestController
@RequestMapping("/files")
public class FilesController {
	
	@Autowired
	private FilesService filesService;
	@Autowired
	private ControllerUtils controllerUtils;
	@Autowired
	private OfertaRepository ofertaRepository;
	@Autowired
	private EmpresaAutotransporteRepository empresaAutotransporteRepository;

	@GetMapping("/images/{filename}")
	public ResponseEntity<?> getImage(@PathVariable String filename){
		try {
			Resource r = filesService.loadImage(filename);
			return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(r);
		} catch (Exception e) {
			return ControllerUtils.exeptionsResponse(e);
		}
	}
	
	@GetMapping("/pdf/{filename}")
	public ResponseEntity<?> getPdf(@PathVariable String filename){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (!ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE) && 
				!ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			return ControllerUtils.unauthorisedResponse();
		}
		try {
			Oferta o = ofertaRepository.findByContrato(filename).orElse(null);
			EmpresaAutotransporte ea = empresaAutotransporteRepository.findByDocumentoFiscal(filename).orElse(null);;
			if(!controllerUtils.perteneceAlUsuario(u, ea) && !controllerUtils.perteneceAlUsuario(u, o))
				return ControllerUtils.unauthorisedResponse();
			Resource r = filesService.loadPdf(filename);
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(r);
		} catch (Exception e) {
			return ControllerUtils.exeptionsResponse(e);
		}
	}
}
