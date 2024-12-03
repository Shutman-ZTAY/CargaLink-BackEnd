package com.ipn.mx.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.RepresentanteClienteSeguro;
import com.ipn.mx.model.dto.RepresentanteTransporteSeguro;
import com.ipn.mx.model.dto.UpdateEmpresaCliente;
import com.ipn.mx.model.dto.UpdateEmpresaTransporte;
import com.ipn.mx.model.dto.UpdateRepcli;
import com.ipn.mx.model.dto.UpdateReptrans;
import com.ipn.mx.model.entity.Empresa;
import com.ipn.mx.model.entity.EmpresaAutotransporte;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.EmpresaAutotransporteRepository;
import com.ipn.mx.model.repository.EmpresaRepository;
import com.ipn.mx.model.repository.RepresentanteClienteRepository;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;

@RestController
@RequestMapping("/representante")
public class PerfilesRepresentantesController {
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private RepresentanteClienteRepository rcr;
	@Autowired
	private EmpresaAutotransporteRepository ear;
	@Autowired
	private EmpresaRepository er;
	@Autowired
	private PasswordEncoder pe;
	
	@GetMapping("/transporte/detalles")
	public ResponseEntity<?> viewMyDetailsRepresentanteTransporte(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				RepresentanteTransporte rt = 
						rtr.findById(u.getIdUsuario()).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
				RepresentanteTransporteSeguro rts = (RepresentanteTransporteSeguro) RepresentanteTransporteSeguro.usuarioToUsuarioSeguro(rt);
				return ControllerUtils.okResponse(rts);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		}else
			return ControllerUtils.unauthorisedResponse();
	}

	@PutMapping("/transporte/modificar")
	public ResponseEntity<?> updateRepTransInfo(@RequestBody UpdateReptrans updateReptrans){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Usuario u = (Usuario) auth.getPrincipal();
			try {
			RepresentanteTransporte rt = 
					rtr.findById(u.getIdUsuario()).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
			
					if(!pe.matches(updateReptrans.getPassword(), u.getPassword())) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contraseña Incorrecta");
					}
					String newNombre = rt.getNombre();
					String newPrimerApellido = rt.getPrimerApellido();
					String newSegundoApellido = rt.getSegundoApellido();
					String newCorreo = rt.getCorreo();
					String newTelefono = rt.getTelefono();
					String newPassword = rt.getPassword();
					if(updateReptrans.getNombre() != null)
						newNombre = updateReptrans.getNombre();
					if(updateReptrans.getPrimerApellido() != null)
						newPrimerApellido = updateReptrans.getPrimerApellido();
					if(updateReptrans.getSegundoApellido() != null)
						newSegundoApellido = updateReptrans.getSegundoApellido();
					if(updateReptrans.getCorreo() != null)
						newCorreo = updateReptrans.getCorreo();
					if(updateReptrans.getTelefono() != null)
						newTelefono = updateReptrans.getTelefono();
					if(updateReptrans.getNewpass() != null)
						newPassword = pe.encode(updateReptrans.getNewpass());
					rtr.updateReptrans(u.getIdUsuario(), newPassword, newTelefono, newNombre, newPrimerApellido, newSegundoApellido, newCorreo);
					return ResponseEntity.ok(null);
			}catch(Exception e){
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	@PutMapping("/transporte/modificar/empresa")
	public ResponseEntity<?> updateEmpTransInfo(@RequestBody UpdateEmpresaTransporte updateEmpresaTransporte){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			Usuario u = (Usuario) auth.getPrincipal();
			try {
			RepresentanteTransporte rt = 
					rtr.findById(u.getIdUsuario()).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
					if(!pe.matches(updateEmpresaTransporte.getPassword(), u.getPassword())) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contraseña Incorrecta");
					}
			EmpresaAutotransporte ea = rt.getEmpresaTransporte();
					String newNombreComercial = ea.getNombreComercial();
					String newRFC = ea.getRfc();
					String newDireccion = ea.getDireccion();
					String newDescripcion = ea.getDescripcion();
					
					if(updateEmpresaTransporte.getNombreComercial()!=null)
						newNombreComercial = updateEmpresaTransporte.getNombreComercial();
					if(updateEmpresaTransporte.getRfc()!=null)
						newRFC = updateEmpresaTransporte.getRfc();
					if(updateEmpresaTransporte.getDireccion()!=null) 
						newDireccion = updateEmpresaTransporte.getDireccion();
					if(updateEmpresaTransporte.getDescripcion()!=null)
						newDescripcion = updateEmpresaTransporte.getDescripcion();
					
					ear.updateEmpresaTransporte(ea.getRazonSocial(), newNombreComercial, newRFC, newDireccion, newDescripcion);
					return ResponseEntity.ok(null);
			}catch(Exception e){
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	@GetMapping("/cliente/detalles")
	public ResponseEntity<?> viewMyDetailsRepresentanteCliente(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if(ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			try {
				RepresentanteCliente rc = rcr.findById(u.getIdUsuario()).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
				RepresentanteClienteSeguro rcs = (RepresentanteClienteSeguro) RepresentanteClienteSeguro.reprClienteToRepresentanteClienteSeguro(rc);
				return ControllerUtils.okResponse(rcs);
			} catch(Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@PutMapping("/cliente/modificar")
	public ResponseEntity<?> updateRepCliInfo(@RequestBody UpdateRepcli updateRepcli){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			Usuario u = (Usuario) auth.getPrincipal();
			try {
			RepresentanteCliente rc = 
					rcr.findById(u.getIdUsuario()).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
			
					if(!pe.matches(updateRepcli.getPassword(), u.getPassword())) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contraseña Incorrecta");
					}
					String newNombre = rc.getNombre();
					String newPrimerApellido = rc.getPrimerApellido();
					String newSegundoApellido = rc.getSegundoApellido();
					String newCorreo = rc.getCorreo();
					String newTelefono = rc.getTelefono();
					String newPassword = rc.getPassword();
					if(updateRepcli.getNombre() != null)
						newNombre = updateRepcli.getNombre();
					if(updateRepcli.getPrimerApellido() != null)
						newPrimerApellido = updateRepcli.getPrimerApellido();
					if(updateRepcli.getSegundoApellido() != null)
						newSegundoApellido = updateRepcli.getSegundoApellido();
					if(updateRepcli.getCorreo() != null)
						newCorreo = updateRepcli.getCorreo();
					if(updateRepcli.getTelefono() != null)
						newTelefono = updateRepcli.getTelefono();
					if(updateRepcli.getNewpass() != null)
						newPassword = pe.encode(updateRepcli.getNewpass());
					rcr.updateRepcli(u.getIdUsuario(), newPassword, newTelefono, newNombre, newPrimerApellido, newSegundoApellido, newCorreo);
					return ResponseEntity.ok(null);
			}catch(Exception e){
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	@PutMapping("/cliente/modificar/empresa")
	public ResponseEntity<?> updateEmpCliInfo(@RequestBody UpdateEmpresaCliente updateEmpresaCliente){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_CLIENTE)) {
			Usuario u = (Usuario) auth.getPrincipal();
			try {
			RepresentanteCliente rc = 
					rcr.findById(u.getIdUsuario()).orElseThrow(() -> new NoSuchElementException("Recurso no encontrado"));
					if(!pe.matches(updateEmpresaCliente.getPassword(), u.getPassword())) {
						return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Contraseña Incorrecta");
					}
			Empresa ec = rc.getEmpresaCliente();
					String newNombreComercial = ec.getNombreComercial();
					String newRFC = ec.getRfc();
					String newDireccion = ec.getDireccion();
					String newDescripcion = ec.getDescripcion();
					
					if(updateEmpresaCliente.getNombreComercial()!=null)
						newNombreComercial = updateEmpresaCliente.getNombreComercial();
					if(updateEmpresaCliente.getRfc()!=null)
						newRFC = updateEmpresaCliente.getRfc();
					if(updateEmpresaCliente.getDireccion()!=null) 
						newDireccion = updateEmpresaCliente.getDireccion();
					if(updateEmpresaCliente.getDescripcion()!=null)
						newDescripcion = updateEmpresaCliente.getDescripcion();
					
					er.updateEmpresaCliente(ec.getRazonSocial(), newNombreComercial, newRFC, newDireccion, newDescripcion);
					return ResponseEntity.ok(null);
			}catch(Exception e){
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
}
