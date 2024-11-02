package com.ipn.mx.controller;

import java.time.LocalDate;
import java.time.LocalTime;
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

import com.ipn.mx.exeptions.InvalidRequestExeption;
import com.ipn.mx.model.dto.CreateOferta;
import com.ipn.mx.model.dto.OfertaDTO;
import com.ipn.mx.model.dto.TokenViaje;
import com.ipn.mx.model.dto.UpdEstatus;
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
import com.ipn.mx.model.repository.TransportistaRepository;
import com.ipn.mx.service.interfaces.JwtService;

import io.jsonwebtoken.Claims;

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
	private ControllerUtils controllerUtils;
	@Autowired
	private JwtService jwtService;
	
	private static EstatusOferta[] UPDATABLE_STATUS = 
		{EstatusOferta.EMBARCANDO, EstatusOferta.EN_CAMINO, 
			EstatusOferta.PROBLEMA, EstatusOferta.ENTREGADO};

	@PostMapping("/representante/cliente/oferta")
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

	@GetMapping("/representante/cliente/oferta")
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
	
	//Si el usuario es administrador retorna todas las ofertas aun que no se pueda postular a ellas
	@GetMapping("/representante/transporte/oferta")
	public ResponseEntity<?> viewAllOfertas(){
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
	
	@GetMapping("/transportista/oferta")
	public ResponseEntity<?> viewFromTransportista(@RequestBody String idTransportista){
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
	
	@PatchMapping("/transportista/oferta")
	public ResponseEntity<?> updateEstatusFromTransportista(@RequestBody(required = false) UpdEstatus updEstatus){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.TRANSPORTISTA)) {
			try {
				Oferta o = ofertaRepository.findByIdTransportista(u.getIdUsuario()).orElseThrow(() -> new NoSuchElementException("Elemento no encontrado"));
				boolean update = verifyStatus(updEstatus);
				if (!update || updEstatus == null)
					throw new InvalidRequestExeption("Estatus no valido");
				if (updEstatus.getEstatus() == EstatusOferta.EN_CAMINO) {
					o.setEstatus(updEstatus.getEstatus());
					o.setFechaInicio(LocalDate.now()); o.setHoraInicio(LocalTime.now());
					ofertaRepository.save(o);
					return ControllerUtils.okResponse();
				} else {
					ofertaRepository.updateEstatusOferta(o.getIdOferta(), updEstatus.getEstatus());
					return ControllerUtils.okResponse();
				}
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	@PatchMapping("/representante/cliente/oferta/finalizar")
	public ResponseEntity<?> finalizarViaje(@RequestBody(required = true) TokenViaje token){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				Claims c = jwtService.getAllClaims(token.getToken());
				Oferta oferta = ofertaRepository.findById(c.get("idOferta", Integer.class))
						.orElseThrow(() -> new NoSuchElementException("Oferta no encontrada"));
				if (!oferta.getTokenViaje().equals(token.getToken()) || !controllerUtils.perteneceAlUsuario(u, oferta)) {
					return ControllerUtils.unauthorisedResponse();
				}
				oferta.setHoraTermino(LocalTime.now());
				oferta.setFechaFin(LocalDate.now());
				oferta.setEstatus(EstatusOferta.FINALIZADO);
				ofertaRepository.save(oferta);
				return ControllerUtils.okResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@PatchMapping("/representante/cliente/oferta/pagar/{idOferta}")
	public ResponseEntity<?> pagarViaje(@PathVariable Integer idOferta){
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
				return ControllerUtils.okResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	private boolean verifyStatus(UpdEstatus updEstatus) {
		boolean update = false;
		for (EstatusOferta estatusOferta : UPDATABLE_STATUS) {
			if (estatusOferta == updEstatus.getEstatus()) {
				update = true;
			}
		}
		return update;
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
