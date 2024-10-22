package com.ipn.mx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
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
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.SedeDTO;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Sede;
import com.ipn.mx.model.entity.Semirremolque;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;
import com.ipn.mx.model.repository.SedeRepository;
import com.ipn.mx.model.repository.SemirremolqueRepository;

@RestController
@RequestMapping("/representante/transporte/semirremolque")
public class SemiremolqueController {

	@Autowired
	private SemirremolqueRepository semirremolqueRepository;
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private SedeRepository sedeRepository;
	
	@PostMapping("")
	public ResponseEntity<?> crearSemirremolque(@RequestBody(required = true) Semirremolque semirremolque){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Usuario u = (Usuario) auth.getPrincipal();
			try {
				if (perteneceAlUsuario(u, semirremolque.getSede())) {
					semirremolqueRepository.save(semirremolque);
					return ControllerUtils.createdResponse();
				} else 
					return ControllerUtils.badRequestResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@GetMapping("")
	public ResponseEntity<?> viewAllSemirremolques(@RequestBody(required = false) String idRepresentanteTrans){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Usuario u = (Usuario) auth.getPrincipal();
			RepresentanteTransporte rt;
			try {
				if(u.getRol() == RolUsuario.REPRESENTANTE_TRANSPORTE)
					rt = rtr.findById(u.getIdUsuario()).get();
				else
					rt = rtr.findById(idRepresentanteTrans)
					.orElseThrow(() -> new NoSuchElementException("No se encontró el representante con ID: " + idRepresentanteTrans));
			
				List<Semirremolque> ls = findSemirremolquesByRepresentante(rt);
				return ControllerUtils.okResponse(ls);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@PutMapping("/{idSemirremolque}")
	public ResponseEntity<?> updateSemirremolque(
			@PathVariable Integer idSemirremolque, 
			@RequestBody(required = true) Semirremolque semirremolque){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Usuario u = (Usuario) auth.getPrincipal();
			semirremolque.setIdSemirremolque(idSemirremolque);
			try {
				if (perteneceAlUsuario(u, semirremolque)) {
					semirremolque = getUpdateSemirremolque(semirremolque);
					semirremolqueRepository.save(semirremolque);
					return ControllerUtils.okResponse();
				} else 
					return ControllerUtils.badRequestResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@DeleteMapping("{idSemirremolque}")
	public ResponseEntity<?> deleteSemirremolque(@PathVariable(required = true) Integer idSemirremolque){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				Usuario u = (Usuario) auth.getPrincipal();
				Semirremolque semirremolque = semirremolqueRepository.findById(idSemirremolque)
						.orElseThrow(() -> new NoSuchElementException("No se encontró el semirremolque con ID: " + idSemirremolque));
				if (perteneceAlUsuario(u, semirremolque)) {
					semirremolqueRepository.deleteById(idSemirremolque);
					return ControllerUtils.okResponse();
				} else 
					return ControllerUtils.badRequestResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else
			return ControllerUtils.unauthorisedResponse();
	}
	
	private boolean perteneceAlUsuario(Usuario u, Sede sede){
		if (sede == null) {
			return false;
		}
		if (u.getRol() == RolUsuario.ADMINISTRADOR) {
			RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
			Optional<SedeDTO> os = 
					sedeRepository.findSedeByEmpresaAndId(sede.getIdSede(), rt.getEmpresaTransporte().getRazonSocial());
			if(!os.isEmpty())
				return true;
			else
				return false;
		} else {
			return true;
		}
	}
	
	private boolean perteneceAlUsuario(Usuario u, Semirremolque semirremolque){
		if (semirremolque == null) {
			return false;
		}
		Optional<Semirremolque> os = semirremolqueRepository.findById(semirremolque.getIdSemirremolque());
		if(!os.isEmpty() || u.getRol() == RolUsuario.ADMINISTRADOR)
			return perteneceAlUsuario(u, os.get().getSede());
		else
			return false;
		
	}
	
	private List<Semirremolque> findSemirremolquesByRepresentante(Usuario usuario) {
		RepresentanteTransporte rt = rtr.findById(usuario.getIdUsuario()).get();
		List<SedeDTO> ls = sedeRepository.findAllSedesByEmpresaTransporte(rt.getEmpresaTransporte().getRazonSocial());
		List<Integer> idSedes = new ArrayList<>();
		for (SedeDTO sede : ls) {
			idSedes.add(sede.getIdSede());
		}
		return semirremolqueRepository.findAllSemirremolquesBySedes(idSedes);
	}
	
	private Semirremolque getUpdateSemirremolque(Semirremolque semirremolque) {
		Semirremolque sUpdate = semirremolqueRepository.findById(semirremolque.getIdSemirremolque()).get();
		if (semirremolque.getNombreIdentificador() != null)
			sUpdate.setNombreIdentificador(semirremolque.getNombreIdentificador());
		if (semirremolque.getEstatus() != null)
			sUpdate.setEstatus(semirremolque.getEstatus());
		if (semirremolque.getTipo() != null)
			sUpdate.setTipo(semirremolque.getTipo());
		if (semirremolque.getLargo() != null)
			sUpdate.setLargo(semirremolque.getLargo());
		if (semirremolque.getAncho() != null)
			sUpdate.setAncho(semirremolque.getAncho());
		if (semirremolque.getAlto() != null)
			sUpdate.setAlto(semirremolque.getAlto());
		if (semirremolque.getPeso() != null)
			sUpdate.setPeso(semirremolque.getPeso());
		if (semirremolque.getNoEjes() != null)
			sUpdate.setNoEjes(semirremolque.getNoEjes());
		if (semirremolque.getNoLlantas() != null)
			sUpdate.setNoLlantas(semirremolque.getNoLlantas());
		if (semirremolque.getSede() != null)
			sUpdate.setSede(semirremolque.getSede());
		
		return sUpdate;
	}

}
