package com.ipn.mx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ipn.mx.exeptions.RecursoInvalidoExeption;
import com.ipn.mx.model.dto.EstatusRecurso;
import com.ipn.mx.model.dto.PrecioDefinitivo;
import com.ipn.mx.model.dto.RecursoDTO;
import com.ipn.mx.model.entity.CamionUnitario;
import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.entity.Recurso;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.CategoriaTransportista;
import com.ipn.mx.model.enumerated.EstatusOferta;
import com.ipn.mx.model.enumerated.EstatusTransportista;
import com.ipn.mx.model.enumerated.EstatusVehiculo;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.OfertaRepository;
import com.ipn.mx.model.repository.RecursoRepository;
import com.ipn.mx.model.repository.SemirremolqueRepository;
import com.ipn.mx.model.repository.TransportistaRepository;
import com.ipn.mx.model.repository.VehiculoRepository;
import com.ipn.mx.service.interfaces.FilesService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/representante")
public class RecursoController {
	
	@Autowired
	private ControllerUtils controllerUtils;
	@Autowired
	private RecursoRepository recursoRepository;
	@Autowired
	private OfertaRepository ofertaRepository;
	@Autowired
	private VehiculoRepository vehiculoRepository;
	@Autowired
	private TransportistaRepository transportistaRepository;
	@Autowired
	private SemirremolqueRepository semirremolqueRepository;
	@Autowired
	private FilesService filesService;

	//RF15	Asignar recursos
	@PostMapping(value = "/transporte/recurso/{idOferta}", consumes = { "application/octet-stream", "multipart/form-data" })
	public ResponseEntity<?> createRecursos(
			@PathVariable Integer idOferta,
			@RequestPart(name = "recursos",required = true) List<RecursoDTO> recursosDTO,
			@RequestPart(required = true) PrecioDefinitivo precio,
			@RequestPart(name = "file", required = true) MultipartFile file){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			String filename = null;
			try {
				Oferta o = ofertaRepository.findById(idOferta).orElseThrow(() -> new NoSuchElementException("Oferta no encontrada"));
				if(!controllerUtils.perteneceAlUsuario(u, o))
					return ControllerUtils.unauthorisedResponse();
				if(o.getEstatus() != EstatusOferta.CONFIGURANDO)
					return ControllerUtils.unauthorisedResponse();
				if(file.isEmpty())
					return ControllerUtils.badRequestResponse("Es necesario adjuntar un contrato");
				
				filename = filesService.savePdf(file);
				
				List<Recurso> recursos = verifyRecursos(recursosDTO, o);
				o.getRecursos().clear(); o.getRecursos().addAll(recursos); o.setPrecio(precio.getPrecio());
				o = ofertaRepository.save(o);
				
				ofertaRepository.updateEstatusOferta(idOferta, EstatusOferta.RECOGIENDO);
				ofertaRepository.updateContrato(idOferta, filename);
				
				setEstatusRecursos(recursos, true);
				return ControllerUtils.createdResponse();
			} catch (Exception e) {
				if(filename != null)
					filesService.delete(filename);
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}

	//RF15	Asignar recursos
	// Obtiene todos los recursos asociados a una oferta
	@GetMapping({"/transporte/recurso/{idOferta}", "/cliente/recurso/{idOferta}"})
	public ResponseEntity<?> viewAllRecursoByOferta(@PathVariable Integer idOferta){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE) || 
				ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				Oferta o = ofertaRepository.findById(idOferta).orElseThrow(() -> new NoSuchElementException("Oferta no encontrada"));
				if(!controllerUtils.perteneceAlUsuario(u, o))
					return ControllerUtils.unauthorisedResponse();
				
				return ControllerUtils.okResponse(
							getRecursosDTO(o.getRecursos())
						);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	//RF15	Asignar recursos
	// Borra los recursos que se tenian en la oferta y pone otros que proporciona el representante de transporte
	@PutMapping(value = "/transporte/recurso/{idOferta}", consumes = { "application/octet-stream" })
	public ResponseEntity<?> updeteRecursos(
			@PathVariable Integer idOferta,
			@RequestPart(name = "recursos", required = true) List<RecursoDTO> recursosDTO,
			@RequestPart(name = "file", required = false) MultipartFile file){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			String filename = null;
			try {
				Oferta o = ofertaRepository.findById(idOferta).orElseThrow(() -> new NoSuchElementException("Oferta no encontrada"));
				if(!controllerUtils.perteneceAlUsuario(u, o))
					return ControllerUtils.unauthorisedResponse();
				if(!file.isEmpty()) {
					filesService.delete(o.getContrato());
					filename = filesService.savePdf(file);
					ofertaRepository.updateContrato(idOferta, filename);
				}
				
				setEstatusRecursos(o.getRecursos(), false);
				o.getRecursos().clear();
				ofertaRepository.save(o);
				
				List<Recurso> recursos = verifyRecursos(recursosDTO, o);
				o.getRecursos().addAll(recursos);
				setEstatusRecursos(recursos, true);
				
				return ControllerUtils.okResponse();
			} catch (Exception e) {
				if(filename != null)
					filesService.delete(filename);
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	
	//RF15	Asignar recursos
	// Elimina solamente un recurso de la base de datos
	@DeleteMapping("/transporte/recurso/{idRecurso}")
	public ResponseEntity<?> deleteRecurso(@PathVariable Integer idRecurso){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				Recurso r = recursoRepository.findById(idRecurso).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
				if(!controllerUtils.perteneceAlUsuario(u, r.getOferta()))
					return ControllerUtils.unauthorisedResponse();
				
				recursoRepository.deleteById(idRecurso);
				List<Recurso> lr = new ArrayList<Recurso>(); lr.add(r);
				setEstatusRecursos(lr, false);
				
				return ControllerUtils.okResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}
	

	private List<RecursoDTO> getRecursosDTO(List<Recurso> recursos) {
		List<RecursoDTO> ldto = new ArrayList<RecursoDTO>();
		for (Recurso recurso : recursos) {
			RecursoDTO dto = RecursoDTO.toRecursoDTO(recurso);
			ldto.add(dto);
		}
		return ldto;
	}

	private List<Recurso> verifyRecursos(List<RecursoDTO> recursosDTO, Oferta o) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		List<Recurso> lr = new ArrayList<Recurso>();
		for (int i = 0; i < recursosDTO.size(); i++) {
			Recurso recurso = Recurso.toRecurso(recursosDTO.get(i));
			recurso.setTransportista(transportistaRepository.findById(recurso.getTransportista().getIdUsuario()).get());
			if (recurso.getVehiculo() == null || !controllerUtils.perteneceAlUsuario(u, recurso.getVehiculo())) {
				throw new RecursoInvalidoExeption("Vehiculo no valido en el recurso: " + i);
			}
			if (recurso.getTransportista() == null || !controllerUtils.perteneceAlUsuario(u, recurso.getTransportista())) {
				throw new RecursoInvalidoExeption("Transportista no valido en el recurso: " + i);
			}
			if (!(recurso.getVehiculo() instanceof CamionUnitario) && !controllerUtils.perteneceAlUsuario(u, recurso.getSemirremolque())) {
				throw new RecursoInvalidoExeption("Semirremolque no valido en el recurso: " + i);
			}
			boolean c1 = 
					(recurso.getTransportista().getCategoria() == CategoriaTransportista.B) 
					&& 
					(!(recurso.getVehiculo() instanceof CamionUnitario));
			boolean c2 =
					(recurso.getTransportista().getCategoria() == CategoriaTransportista.C) 
					&& 
					(recurso.getVehiculo() instanceof CamionUnitario);
			boolean c3 = (recurso.getTransportista().getCategoria() == CategoriaTransportista.B_y_C);
			
			if (!(c1 || c2 || c3)) {
				throw new RecursoInvalidoExeption("Transportista no valido para el recurso: " + c1 + c2 + c3 );
			}
			
			recurso.setOferta(o); recurso.setEstatus(EstatusRecurso.EMBARCANDO);
			lr.add(recurso);
		}
		return lr;
	}
	
	private void setEstatusRecursos(List<Recurso> recursos, boolean enViaje) {
		EstatusTransportista et;
		EstatusVehiculo ev;
		if(enViaje) {
			et = EstatusTransportista.EN_VIAJE;
			ev = EstatusVehiculo.EN_VIAJE;
		} else {
			et = EstatusTransportista.DISPONIBLE;
			ev = EstatusVehiculo.DISPONIBLE;
		}
		for (Recurso recurso : recursos) {
			transportistaRepository.updateEstatusTransportista(
						recurso.getTransportista().getIdUsuario(), et
					);
			vehiculoRepository.updateEstatusVehiculo(
						recurso.getVehiculo().getPlaca(), ev
					);
			if (!(recurso.getVehiculo() instanceof CamionUnitario))
				semirremolqueRepository.updateEstatusSemirremolque(
							recurso.getSemirremolque().getIdSemirremolque(), ev
					);
		}
	}
}
