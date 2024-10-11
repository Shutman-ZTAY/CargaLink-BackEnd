package com.ipn.mx.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.SedeDTO;
import com.ipn.mx.model.dto.TransportistaSeguro;
import com.ipn.mx.model.dto.UpdateTransportista;
import com.ipn.mx.model.dto.UpdateTransportistaRepresentante;
import com.ipn.mx.model.dto.UsuarioId;
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
@RequestMapping("/transportista")
public class TransportistasController {

	@Autowired
	private SedeRepository sr;
	@Autowired
	private PasswordEncoder pe;
	@Autowired
	private TransportistaRepository tr;
	@Autowired
	private RepresentanteTransporteRepository rtr;

	@PostMapping("/representante/transporte/nuevo/transportista")
	public ResponseEntity<?> createTransportista(@RequestBody Transportista transportista){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			transportista.setRol(RolUsuario.TRANSPORTISTA);
			try {
				//TODO preguntarle a Alan si desde el front va a mandar el id o si debe hacer el metodo "existByDireccion para SedeRepository"
				if (tr.existsById(null)) {
					String mensaje = "Ya existe este usuario";
					return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(mensaje);
				}
				if(transportista.getSede() != null 
						&& !sr.existsById(transportista.getSede().getIdSede()) ) {
					try {
						sr.save(transportista.getSede());
					} catch (Exception e) {
						String mensaje = "Sede incompleta, registre los de mas datos";
						return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(mensaje);
					}
				} else if (transportista.getSede() == null && !sr.existsById(transportista.getSede().getIdSede())) {
					String mensaje = "El transportista debe asociarse a una sede";
					return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(mensaje); 
				}
				transportista.setPassword(pe.encode(transportista.getPassword()));
				tr.save(transportista);
				return ResponseEntity.status(HttpStatus.CREATED).body(null);
			} catch (Exception e) {
				String mensajeError = "Error interno en el servidor: " + e.getMessage();
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	@GetMapping("/representante/transporte/obtener/transportista")
	public ResponseEntity<?> findAllTransportistasByRepresentante(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			RepresentanteTransporte rtAuth = (RepresentanteTransporte) auth.getPrincipal();
			try {
				rtAuth = rtr.findById(rtAuth.getIdUsuario()).get();
				List<TransportistaSeguro> l = findTransportistasByEmpresa(rtAuth.getEmpresaTransporte().getRazonSocial());
				return ResponseEntity.ok(l);
			} catch (Exception e) {
				return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error interno en el servidor: " + e.getMessage());
			}
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
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

	@PutMapping("/representante/transporte/modificar/transportista")
	public ResponseEntity<?> updateTransportistaFromRepresentante(
			@RequestBody UpdateTransportistaRepresentante updateTransportistaRepresentante){
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Transportista tbd = tr.findById(updateTransportistaRepresentante.getIdUsuario()).get();

			Integer newExperiencia = tbd.getExperiencia();
			CategoriaTransportista newCategoria = tbd.getCategoria();
			Integer newSede = tbd.getSede().getIdSede();
			
			if (updateTransportistaRepresentante.getExperiencia() != null)
				newExperiencia = updateTransportistaRepresentante.getExperiencia();
			if (updateTransportistaRepresentante.getCategoria() != null) 
				newCategoria = updateTransportistaRepresentante.getCategoria();
			if (updateTransportistaRepresentante.getSede() != null)
				newSede = updateTransportistaRepresentante.getSede().getIdSede();
			if(!sr.existsById(newSede)) {
				try {
					sr.save(updateTransportistaRepresentante.getSede());
				} catch (Exception e) {
					String mensaje = "Sede incompleta, registre los de mas datos";
					return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(mensaje);
				}
			}
			
			try {
				tr.updateTransportistaFromRepresentante(tbd.getIdUsuario(), newExperiencia, newCategoria, newSede);
				return ResponseEntity.ok(null);
			} catch (Exception e) {
				return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body(e.getMessage());
			}
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	@DeleteMapping("/representante/transporte/borrar/transportista")
	public ResponseEntity<?> deleteTransportista(@RequestBody UsuarioId transportistaId){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			tr.deleteById(transportistaId.getIdUsuario());
			try {
				return ResponseEntity.ok(null);
			} catch (Exception e) {
				String mensajeError = "Error interno en el servidor";
		        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(mensajeError);
			}
		}else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

	
	//TODO Buscar como cambiar la contraseña con tal de que el usuario se vuelva a loguear
	@PutMapping("/modificar")
	public ResponseEntity<?> updateTransportista(@RequestBody UpdateTransportista updateTransportista){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (isAuthorised(auth, RolUsuario.TRANSPORTISTA)) {
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
				return ResponseEntity
						.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error interno en el servidor: " + e.getMessage());
			}
			
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
	
	private boolean isAuthorised(Authentication auth, RolUsuario rol) {
		Usuario u = (Usuario) auth.getPrincipal();
		if (auth == null || !auth.isAuthenticated()) {
            return false;
        }else if (u.getRol() != rol) {
        	return false;
        }else {
        	return true;
        }
	}
}
