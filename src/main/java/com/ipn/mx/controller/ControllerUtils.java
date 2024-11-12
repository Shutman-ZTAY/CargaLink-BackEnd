package com.ipn.mx.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.ipn.mx.model.dto.SedeDTO;
import com.ipn.mx.model.entity.Empresa;
import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.entity.Postulacion;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Sede;
import com.ipn.mx.model.entity.Semirremolque;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.entity.Vehiculo;
import com.ipn.mx.model.enumerated.EstatusOferta;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.OfertaRepository;
import com.ipn.mx.model.repository.PostulacionRepository;
import com.ipn.mx.model.repository.RepresentanteClienteRepository;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;
import com.ipn.mx.model.repository.SedeRepository;
import com.ipn.mx.model.repository.SemirremolqueRepository;
import com.ipn.mx.model.repository.TransportistaRepository;
import com.ipn.mx.model.repository.UsuarioRepository;
import com.ipn.mx.model.repository.VehiculoRepository;

@Service
public class ControllerUtils {
	
	@Autowired
	private SedeRepository sedeRepository;
	@Autowired
	private RepresentanteTransporteRepository rtr;
	@Autowired
	private RepresentanteClienteRepository rct;
	@Autowired
	private OfertaRepository ofertaRepository;
	@Autowired
	private SemirremolqueRepository semirremolqueRepository;
	@Autowired
	private TransportistaRepository transportistaRepository;
	@Autowired
	private VehiculoRepository vehiculoRepository;
	@Autowired
	private PostulacionRepository postulacionRepository;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public static ResponseEntity<?> exeptionsResponse(Exception e){
		String messageError = "Error interno en el servidor: " + e.getMessage();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageError);
	}
	
	public static ResponseEntity<?> okResponse(Object obj){
		return ResponseEntity.status(HttpStatus.OK).body(obj);
	}
	
	public static ResponseEntity<?> okResponse(){
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
	
	public static ResponseEntity<?> badRequestResponse(){
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
	}

	public static ResponseEntity<?> unauthorisedResponse() {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	}
	
	public static ResponseEntity<?> createdResponse() {
		return ResponseEntity.status(HttpStatus.CREATED).body(null);
	}
	
	public static boolean isAuthorised(Authentication auth, RolUsuario rol) {
		Usuario u = (Usuario) auth.getPrincipal();
		if (auth == null || !auth.isAuthenticated()) {
            return false;
        }else if (u.getRol() == rol || u.getRol() == RolUsuario.ADMINISTRADOR) {
        	return true;
        }else {
        	return false;
        }
	}

	public static ResponseEntity<?> badRequestResponse(String mensaje) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mensaje);
	}
	
	public boolean perteneceAlUsuario(Usuario u, Empresa empresa){
		if (empresa == null) {
			return false;
		}
		u = usuarioRepository.findById(u.getIdUsuario()).get();
		if (u.getRol() == RolUsuario.REPRESENTANTE_TRANSPORTE || u.getRol() == RolUsuario.REPRESENTANTE_CLIENTE) {
			if (u instanceof RepresentanteTransporte) 
				return ((RepresentanteTransporte) u).getEmpresaTransporte().equals(empresa);
			else if (u instanceof RepresentanteCliente)
				return ((RepresentanteCliente) u).getEmpresaCliente().equals(empresa);
			else
				return false;
		} else if (u.getRol() == RolUsuario.ADMINISTRADOR)
			return true;
		else
			return false;
	}
	
	public boolean perteneceAlUsuario(Usuario u, Sede sede){
		if (sede == null) {
			return false;
		}
		if (u.getRol() == RolUsuario.REPRESENTANTE_TRANSPORTE) {
			RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
			Optional<SedeDTO> os = sedeRepository.findSedeByEmpresaAndId(sede.getIdSede(), rt.getEmpresaTransporte().getRazonSocial());
			if(!os.isEmpty())
				return true;
			else
				return false;
		} else if (u.getRol() == RolUsuario.ADMINISTRADOR)
			return true;
		else
			return false;
	}

	public boolean perteneceAlUsuario(Usuario u, Oferta oferta) {
		if (oferta == null) {
			return false;
		}
		if (u.getRol() == RolUsuario.REPRESENTANTE_CLIENTE) {
			boolean exist = ofertaRepository.existByClienteAndId(oferta.getIdOferta(), u.getIdUsuario());
			if(exist)
				return true;
			else
				return false;
		} else if (u.getRol() == RolUsuario.ADMINISTRADOR)
			return true;
		else if (u.getRol() == RolUsuario.REPRESENTANTE_TRANSPORTE) {
			boolean exist = rtr.existByOferta(oferta.getIdOferta());
			if (exist)
				return true;
			else
				return false;
		} else {
			boolean exist = transportistaRepository.existByOferta(oferta.getIdOferta(), u.getIdUsuario());
			if (exist)
				return true;
			else
				return false;
		}
	}
	
	public boolean perteneceAlUsuario(Usuario u, Semirremolque semirremolque){
		if (semirremolque == null || semirremolque.getIdSemirremolque() == null) {
			return false;
		}
		Optional<Semirremolque> os = semirremolqueRepository.findById(semirremolque.getIdSemirremolque());
		if(!os.isEmpty())
			return perteneceAlUsuario(u, os.get().getSede());
		else
			return false;
	}
	
	public boolean perteneceAlUsuario(Usuario u, Transportista transportista){
		if (transportista == null || transportista.getIdUsuario() == null) {
			return false;
		}
		Optional<Transportista> ot = transportistaRepository.findById(transportista.getIdUsuario());
		if(!ot.isEmpty())
			return perteneceAlUsuario(u, ot.get().getSede());
		else
			return false;
		
	}
	
	public boolean perteneceAlUsuario(Usuario u, Vehiculo vehiculo){
		if (vehiculo == null || vehiculo.getPlaca() == null) {
			return false;
		}
		Optional<Vehiculo> ov = vehiculoRepository.findById(vehiculo.getPlaca());
		if(!ov.isEmpty() || u.getRol() == RolUsuario.ADMINISTRADOR)
			return perteneceAlUsuario(u, ov.get().getSede());
		else
			return true;
	}

	public boolean perteneceAlUsuario(Usuario usuario, Postulacion postulacion) {
		if (usuario.getIdUsuario().equals(postulacion.getRepresentanteTransporte().getIdUsuario()))
			return true;
		else if (usuario.getRol() == RolUsuario.ADMINISTRADOR) 
			return true;
		else
			return false;
	}
}
