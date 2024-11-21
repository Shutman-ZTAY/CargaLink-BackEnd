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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.CreateOferta;
import com.ipn.mx.model.dto.OfertaDTO;
import com.ipn.mx.model.entity.Calificacion;
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
import com.ipn.mx.model.repository.CalificacionRepository;
import com.ipn.mx.model.repository.CargaRepository;
import com.ipn.mx.model.repository.OfertaRepository;
import com.ipn.mx.model.repository.RepresentanteClienteRepository;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;
import com.ipn.mx.model.repository.TransportistaRepository;
import com.ipn.mx.service.interfaces.SentimentAnalysisService;
import com.ipn.mx.service.interfaces.VectorEmpresaService;

@RestController
@RequestMapping("")
public class OfertaController {
	
	@Autowired
	private OfertaRepository ofertaRepository;
	@Autowired
	private RepresentanteClienteRepository rcr;
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private TransportistaRepository transportistaRepository;
	@Autowired
	private CargaRepository cargaRepository;
	@Autowired
	private CalificacionRepository calificacionRepository;
	@Autowired
	private ControllerUtils controllerUtils;
	@Autowired
	private VectorEmpresaService vectorEmpresaService;
	@Autowired
	private SentimentAnalysisService analysisService;

	//RF10	Publicar ofertas de trabajo
	@PostMapping("/representante/cliente/oferta")
	public ResponseEntity<?> createOferta(@RequestBody(required = true) CreateOferta createOferta){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				Oferta oferta = Oferta.toOferta(createOferta);
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

	//RF12	Gestionar ofertas
	@GetMapping("/representante/cliente/oferta")
	public ResponseEntity<?> viewAllOfertasByRepresentante(
			@RequestParam(required = false, name = "idRepresentanteCliente") String idRepresentanteCliente){
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
	
	//RF12	Gestionar ofertas
	//RF11	Postulación de empresas de autotransporte
	@GetMapping(value = {"/representante/cliente/oferta/{idOferta}", "/representante/transporte/oferta/{idOferta}", "/transportista/oferta/{idOferta}"})
	public ResponseEntity<?> viewOfertaById(@PathVariable Integer idOferta){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		
			try {
				Oferta o = ofertaRepository.findById(idOferta).orElseThrow(() -> new NoSuchElementException("Oferta no encontrada"));
				if (u.getRol() == RolUsuario.REPRESENTANTE_TRANSPORTE ) 
					return ControllerUtils.okResponse(OfertaDTO.ofertatoOfertaDTO(o));
				if (!controllerUtils.perteneceAlUsuario(u, o))
					return ControllerUtils.unauthorisedResponse();
				
				return ControllerUtils.okResponse(OfertaDTO.ofertatoOfertaDTO(o));
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		
	}

	//RF12	Gestionar ofertas
	@DeleteMapping("/representante/cliente/oferta/{idOferta}")
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
	
	//RF11	Postulación de empresas de autotransporte
	//Si el usuario es administrador retorna todas las ofertas aun que no se pueda postular a ellas
	@GetMapping("/representante/transporte/oferta")
	public ResponseEntity<?> viewAllOfertasPostulables(){
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
								toOfertaDTO(ofertaRepository.findAll())
							);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	//RF17	Realizar viaje
	@GetMapping("/transportista/oferta")
	public ResponseEntity<?> viewFromTransportista(@RequestParam(required = false) String idTransportista){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.TRANSPORTISTA)) {
			try {
				if (u.getRol() == RolUsuario.ADMINISTRADOR)
					u = transportistaRepository.findById(idTransportista).orElseThrow(() -> new NoSuchElementException());
				Oferta o = ofertaRepository.findByIdTransportista(u.getIdUsuario()).orElseThrow(() -> new NoSuchElementException("Elemento no encontrado"));
				return ControllerUtils.okResponse(OfertaDTO.ofertatoOfertaDTO(o));
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	//RF18	Finalizar viaje
	@PatchMapping("/representante/cliente/oferta/pagar/{idOferta}")
	public ResponseEntity<?> pagarCalificarOferta(
			@PathVariable Integer idOferta,
			@RequestBody Calificacion calificacion){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				Oferta oferta = ofertaRepository.findById(idOferta)
						.orElseThrow(() -> new NoSuchElementException("Oferta no encontrada"));
				if (!controllerUtils.perteneceAlUsuario(u, oferta)) {
					return ControllerUtils.unauthorisedResponse();
				}
				ofertaRepository.updateEstatusOferta(oferta.getIdOferta(), EstatusOferta.PAGADO);
				calificacion.setOferta(oferta);
				
				calificacion = analysisService.setScores(calificacion); //TODO Hacer prueba unitaria de este servicio
				vectorEmpresaService.changeAverge(calificacion);		//TODO Hacer prueba unitaria de este servicio
				calificacionRepository.save(calificacion);
				return ControllerUtils.okResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
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
