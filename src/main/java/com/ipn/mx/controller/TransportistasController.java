package com.ipn.mx.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.CategoriaTransportista;
import com.ipn.mx.model.enumerated.EstatusTransportista;
import com.ipn.mx.model.enumerated.RolUsuario;
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
				if(transportista.getSede() != null 
						&& !sr.existsById(transportista.getSede().getIdSede()) ) {
					try {
						sr.save(transportista.getSede());
					} catch (Exception e) {
						String mensaje = "Sede incompleta, registre los de mas datos";
						return ControllerUtils.badRequestResponse(mensaje);
					}
				} else if (transportista.getSede() == null && !sr.existsById(transportista.getSede().getIdSede())) {
					String mensaje = "El transportista debe asociarse a una sede";
					return ControllerUtils.badRequestResponse(mensaje); 
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

	@GetMapping("/representante/transporte/transportista")
	public ResponseEntity<?> findAllTransportistasByRepresentante(@RequestParam(required = false) String idRepresentanteTrans){
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
			
			if (!controllerUtils.perteneceAlUsuario((Usuario) auth.getPrincipal(), tr.findById(id).orElse(null)))
				return ControllerUtils.badRequestResponse();
			if(!sr.existsById(newSede)) {
				try {
					sr.save(updateTransportistaRepresentante.getSede());
				} catch (Exception e) {
					return ControllerUtils.exeptionsResponse(e);
				}
			}
			
			try {
				tr.updateTransportistaFromRepresentante(tbd.getIdUsuario(), newExperiencia, newCategoria, newSede);
				return ResponseEntity.ok(null);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	@DeleteMapping("/representante/transporte/transportista/{id}")
	public ResponseEntity<?> deleteTransportista(@PathVariable String id){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				if (controllerUtils.perteneceAlUsuario((Usuario) auth.getPrincipal(), tr.findById(id).orElse(null))) {
					tr.deleteById(id);
					return ResponseEntity.ok(null);
				} else {
					return ControllerUtils.badRequestResponse();
				}
			} catch (Exception e) {
		        return ControllerUtils.exeptionsResponse(e);
			}
		}else {
			return ControllerUtils.unauthorisedResponse();
		}
	}

	//TODO Buscar como cambiar la contraseña con tal de que el usuario se vuelva a loguear
	@PutMapping("transportista/editar")
	public ResponseEntity<?> updateTransportista(@RequestBody UpdateTransportista updateTransportista){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (ControllerUtils.isAuthorised(auth, RolUsuario.TRANSPORTISTA)) {
			Transportista transportistaAuth = (Transportista) auth.getPrincipal();
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
			
			try {
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
