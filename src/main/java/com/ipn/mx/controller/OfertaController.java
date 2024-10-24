package com.ipn.mx.controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.CreateOferta;
import com.ipn.mx.model.dto.OfertaDTO;
import com.ipn.mx.model.entity.Carga;
import com.ipn.mx.model.entity.Contenedor;
import com.ipn.mx.model.entity.Embalaje;
import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.EstatusOferta;
import com.ipn.mx.model.enumerated.EstatusRepTrans;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.enumerated.TipoCarga;
import com.ipn.mx.model.repository.CargaRepository;
import com.ipn.mx.model.repository.OfertaRepository;
import com.ipn.mx.model.repository.RepresentanteClienteRepository;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;

@RestController
@RequestMapping("/representante")
public class OfertaController {
	
	@Autowired
	private OfertaRepository ofertaRepository;
	@Autowired
	private RepresentanteClienteRepository rcr;
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private CargaRepository cargaRepository;
	@Autowired
	private ControllerUtils controllerUtils;

	@PostMapping("/cliente/oferta")
	public ResponseEntity<?> createOferta(@RequestBody(required = true) CreateOferta createOferta){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				Oferta oferta = CreateOferta.toOferta(createOferta);
				oferta.setRepresentanteCliente((RepresentanteCliente) u);
				oferta.setEstatus(EstatusOferta.OFERTA);
				if(oferta.getCargas() == null)
					return ControllerUtils.badRequestResponse("La oferta no tiene cargas asociadas");
				oferta.setCargas(setTipoCarga(oferta.getCargas()));
				Oferta o = ofertaRepository.save(oferta);
				cargaRepository.saveAll(setIdOfertaIdInCargas(oferta.getCargas(), o));
				return ControllerUtils.createdResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}

	@GetMapping("/cliente/oferta")
	public ResponseEntity<?> viewAllOfertasByRepresentante(@RequestParam(required = false) String idRepresentanteCliente){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			RepresentanteCliente rc;
			try {
				if (u.getRol() == RolUsuario.ADMINISTRADOR)
					rc = rcr.findById(idRepresentanteCliente).get();
				else
					rc = rcr.findById(u.getIdUsuario()).get();
				
				List<Oferta> lo = ofertaRepository.findAllOfertasByReprCliente(rc.getIdUsuario());
				return ControllerUtils.okResponse(toOfertaDTO(lo));
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}

	@DeleteMapping("/cliente/oferta/{idOferta}")
	public ResponseEntity<?> deleteOferta(@PathVariable Integer idOferta){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			Oferta o = ofertaRepository.findById(idOferta).orElseThrow(
					() -> new NoSuchElementException("No existe este recurso"));
			if (controllerUtils.perteneceAlUsuario(u, o)) {
				try {
					ofertaRepository.deleteById(o.getIdOferta());
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
	
	@GetMapping("/transporte/oferta")
	public ResponseEntity<?> viewAllOfertas(@RequestParam(required = false) String idRepresentanteCliente){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			if (u.getRol() != RolUsuario.ADMINISTRADOR) {
				RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
				if (rt.getEstatusRepTrans() != EstatusRepTrans.VALIDO)
					return ControllerUtils.unauthorisedResponse();
			}
			try {
				if (u.getRol() == RolUsuario.REPRESENTANTE_TRANSPORTE) {
					List<Oferta> lo = ofertaRepository.findAllOfertasDisponibles();
					return ControllerUtils.okResponse(toOfertaDTO(lo));
				} else
					return ControllerUtils.okResponse(
								ofertaRepository.findAll()
							);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	private List<Carga> setTipoCarga(List<Carga> cargas) {
		List<Carga> lc = new ArrayList<Carga>(); 
		for (Carga carga : cargas) {
			if (carga instanceof Embalaje)
				carga.setTipo(TipoCarga.EMBALAJE);
			else if (carga instanceof Contenedor)
				carga.setTipo(TipoCarga.CONTENEDOR);
			else
				carga.setTipo(TipoCarga.SUELTA);
			lc.add(carga);
		}
		return lc;
	}
	
	private List<OfertaDTO> toOfertaDTO(List<Oferta> lo) {
		List<OfertaDTO> ldto = new ArrayList<OfertaDTO>();
		for (Oferta oferta : lo) {
			OfertaDTO dto = OfertaDTO.ofertatoOfertaDTO(oferta);
			ldto.add(dto);
		}
		return ldto;
	}
	
	private List<Carga> setIdOfertaIdInCargas(List<Carga> cargas, Oferta oferta) {
		for (Carga carga : cargas) {
			carga.setOferta(oferta);
		}
		return cargas;
	}
}