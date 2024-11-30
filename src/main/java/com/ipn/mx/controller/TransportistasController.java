package com.ipn.mx.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.ipn.mx.model.dto.TransportistaSeguro;
import com.ipn.mx.model.dto.UpdateTransportista;
import com.ipn.mx.model.dto.UpdateTransportistaRepresentante;
import com.ipn.mx.model.entity.Chat;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.CategoriaTransportista;
import com.ipn.mx.model.enumerated.EstatusTransportista;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.ChatRepository;
import com.ipn.mx.model.repository.MensajeRepository;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;
import com.ipn.mx.model.repository.SedeRepository;
import com.ipn.mx.model.repository.TransportistaRepository;

@RestController
@RequestMapping("")
public class TransportistasController {

	@Autowired
	private SedeRepository sr;
	@Autowired
	private PasswordEncoder pe;
	@Autowired
	private TransportistaRepository tr;
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private ControllerUtils controllerUtils;
	@Autowired
	private ChatRepository chatRepository;
	@Autowired
	private MensajeRepository mensajeRepository;


	//RF05	Crear cuentas para transportistas
	@PostMapping("/representante/transporte/transportista")
	public ResponseEntity<?> createTransportista(@RequestBody(required = true) Transportista transportista){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			transportista.setRol(RolUsuario.TRANSPORTISTA);
			try {
				if (tr.existsById(transportista.getIdUsuario())) {
					String mensaje = "Ya existe este usuario";
					return ControllerUtils.badRequestResponse(mensaje);
				}
				if(transportista.getSede() != null && !sr.existsById(transportista.getSede().getIdSede()) ) {
					sr.save(transportista.getSede());
				}
				transportista.setPassword(pe.encode(transportista.getPassword()));
				tr.save(transportista);
				return ControllerUtils.createdResponse();
			} catch (Exception e) {
		        return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}

	//RF06	Gestionar cuentas de transportistas
	@GetMapping("/representante/transporte/transportista")
	public ResponseEntity<?> viewAllTransportistasByRepresentante(
			@RequestParam(required = false, name = "idRepresentanteTrans") String idRepresentanteTrans){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Usuario usr = (Usuario) auth.getPrincipal();
			RepresentanteTransporte rt;
			try {
				if (idRepresentanteTrans == null)
					rt = rtr.findById(usr.getIdUsuario()).get();
				else 
					rt = rtr.findById(idRepresentanteTrans).get();
					
				List<TransportistaSeguro> l = findTransportistasByEmpresa(rt.getEmpresaTransporte().getRazonSocial());
				return ControllerUtils.okResponse(l);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	//RF06	Gestionar cuentas de transportistas
	@GetMapping(value = {"/representante/transporte/transportista/{idTransportista}", "/transportista/profile/{idTransportista}"})
	public ResponseEntity<?> viewTransportistasById(@PathVariable String idTransportista){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE) || ControllerUtils.isAuthorised(auth, RolUsuario.TRANSPORTISTA)) {
			Usuario usr = (Usuario) auth.getPrincipal();
			try {
				Transportista t = tr.findById(idTransportista).orElseThrow(() -> new NoSuchElementException("Transportista no encontrado"));
				if (!controllerUtils.perteneceAlUsuario(usr, t))
					return ControllerUtils.unauthorisedResponse();
				return ControllerUtils.okResponse(TransportistaSeguro.toTransportistaSeguro(t));
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	//RF06	Gestionar cuentas de transportistas
	@PutMapping("/representante/transporte/transportista/{id}")
	public ResponseEntity<?> updateTransportistaFromRepresentante(
			@PathVariable String id,
			@RequestBody(required = true) UpdateTransportistaRepresentante updateTransportistaRepresentante){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Transportista tbd = tr.findById(id).get();

			Integer newExperiencia = tbd.getExperiencia();
			CategoriaTransportista newCategoria = tbd.getCategoria();
			Integer newSede = tbd.getSede().getIdSede();
			
			if (updateTransportistaRepresentante.getExperiencia() != null)
				newExperiencia = updateTransportistaRepresentante.getExperiencia();
			if (updateTransportistaRepresentante.getCategoria() != null) 
				newCategoria = updateTransportistaRepresentante.getCategoria();
			if (updateTransportistaRepresentante.getSede() != null)
				newSede = updateTransportistaRepresentante.getSede().getIdSede();
			
			try {
				Transportista t = tr.findById(id).orElseThrow(() -> new NoSuchElementException("Elemento no encontrado"));
				if (!controllerUtils.perteneceAlUsuario((Usuario) auth.getPrincipal(), t))
					return ControllerUtils.unauthorisedResponse();
				tr.updateTransportistaFromRepresentante(tbd.getIdUsuario(), newExperiencia, newCategoria, newSede);
				return ControllerUtils.okResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	//RF06	Gestionar cuentas de transportistas
	@DeleteMapping("/representante/transporte/transportista/{id}")
	public ResponseEntity<?> deleteTransportista(@PathVariable String id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				Transportista t = tr.findById(id).orElseThrow(() -> new NoSuchElementException("Elemento no encontrado"));
				if (!controllerUtils.perteneceAlUsuario((Usuario) auth.getPrincipal(), t))
					return ControllerUtils.unauthorisedResponse();
				List<Chat> chats = chatRepository.findByUsuario(t);
				for(Chat chat: chats) {
					mensajeRepository.deleteAll(mensajeRepository.findByChatOrderByFechaAsc(chat));
					chatRepository.delete(chat);
				}
				tr.deleteById(id);
				return ControllerUtils.okResponse();
			} catch (Exception e) {
		        return ControllerUtils.exeptionsResponse(e);
			}
		}else {
			return ControllerUtils.unauthorisedResponse();
		}
	}

	//RF06	Gestionar cuentas de transportistas
	@PutMapping("/transportista/editar")
	public ResponseEntity<?> updateTransportista(@RequestBody UpdateTransportista updateTransportista){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (ControllerUtils.isAuthorised(auth, RolUsuario.TRANSPORTISTA)) {
			Transportista transportistaAuth = (Transportista) auth.getPrincipal();
			try {
				transportistaAuth = tr.findById(transportistaAuth.getIdUsuario()).get();
				String newPassword = transportistaAuth.getPassword();
				String newTelefono = transportistaAuth.getTelefono();
				EstatusTransportista newEstatus = transportistaAuth.getEstatusTransportista();
				
				if (updateTransportista.getPassword() != null)
					newPassword = pe.encode(updateTransportista.getPassword());
				if (updateTransportista.getTelefono() != null) 
					newTelefono = updateTransportista.getTelefono();
				if (updateTransportista.getEstatusTransportista() != null)
					newEstatus = updateTransportista.getEstatusTransportista();
				
				tr.updateTransportista(transportistaAuth.getIdUsuario(), newPassword, newTelefono, newEstatus);
				return ResponseEntity.ok(null);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
			
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	@PutMapping("/transportista/gestion")
	public ResponseEntity<?> updateTransportistaInfo(@RequestBody UpdateTransportista updateTransportista){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (ControllerUtils.isAuthorised(auth, RolUsuario.TRANSPORTISTA)) {
			Transportista transportistaAuth = (Transportista) auth.getPrincipal();
			try {
				transportistaAuth = tr.findById(transportistaAuth.getIdUsuario()).orElseThrow(()->
							new IllegalArgumentException("Usuario no encontrado"));
						if(!pe.matches(updateTransportista.getVerifyPass(), transportistaAuth.getPassword())) {
							return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contraseña Incorrecta");
						}
				String newPassword = transportistaAuth.getPassword();
				String newTelefono = transportistaAuth.getTelefono();
				EstatusTransportista newEstatus = transportistaAuth.getEstatusTransportista();
				
				if (updateTransportista.getPassword() != null)
					newPassword = pe.encode(updateTransportista.getPassword());
				if (updateTransportista.getTelefono() != null) 
					newTelefono = updateTransportista.getTelefono();
				if (updateTransportista.getEstatusTransportista() != null)
					newEstatus = updateTransportista.getEstatusTransportista();
				
				tr.updateTransportista(transportistaAuth.getIdUsuario(), newPassword, newTelefono, newEstatus);
				return ResponseEntity.ok(null);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
			
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	private List<TransportistaSeguro> findTransportistasByEmpresa(String razonSocial) {
		List<SedeDTO> ls = sr.findAllSedesByEmpresaTransporte(razonSocial);
		List<Integer> idSedes = new ArrayList<>();
		for (SedeDTO sede : ls) {
			idSedes.add(sede.getIdSede());
		}
		return tr.findAllTransportistasByAllSedes(idSedes);
	}
	
}
