package com.ipn.mx.controller;

import java.util.ArrayList;
import java.util.List;
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
import com.ipn.mx.model.entity.CamionUnitario;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Sede;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.entity.Vehiculo;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.enumerated.TipoVehiculo;
import com.ipn.mx.model.repository.RepresentanteTransporteRepository;
import com.ipn.mx.model.repository.SedeRepository;
import com.ipn.mx.model.repository.VehiculoRepository;

@RestController
@RequestMapping("/representante/transporte/vehiculo")
public class VehiculoControler {
	
	@Autowired
	private VehiculoRepository vehiculoRepository;
	@Autowired 
	private RepresentanteTransporteRepository rtr;
	@Autowired 
	private SedeRepository sedeRepository;

	@PostMapping("")
	public ResponseEntity<?> createVehiculo(@RequestBody Vehiculo vehiculo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				vehiculo = setTipoVehiculo(vehiculo);
				if (perteneceAlUsuario(u, vehiculo.getSede()) && !vehiculoRepository.existsById(vehiculo.getPlaca())) {
					vehiculoRepository.save(vehiculo);
					return ControllerUtils.createdResponse();
				} else {
					String mensaje = "Atributos no validos";
					return ControllerUtils.badRequestResponse(mensaje);
				}
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else 
			return ControllerUtils.unauthorisedResponse();
	}
	
	@GetMapping("")
	public ResponseEntity<?> viewAllVehiculos() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				List<Vehiculo> lv = findTransportistasByRepresentante(u);
				return ControllerUtils.okResponse(lv);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else
			return ControllerUtils.unauthorisedResponse();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateVehiculo(@PathVariable String id, @RequestBody Vehiculo vehiculo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				vehiculo = setTipoVehiculo(vehiculo);
				vehiculo.setPlaca(id);
				if (perteneceAlUsuario(u, vehiculo)) {
					vehiculo.setPlaca(id);
					vehiculoRepository.save(vehiculo);
					return ControllerUtils.okResponse();
				} else 
					return ControllerUtils.badRequestResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteVehiculo(@PathVariable String id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				Vehiculo vehiculo = vehiculoRepository.findById(id).orElse(null);
				if (perteneceAlUsuario(u, vehiculo)) {
					vehiculoRepository.deleteById(vehiculo.getPlaca());
					return ControllerUtils.okResponse();
				} else 
					return ControllerUtils.badRequestResponse();
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else {
			return ControllerUtils.unauthorisedResponse();
		}
	}
	
	private boolean perteneceAlUsuario(Usuario u, Sede sede){
		RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
		Optional<SedeDTO> os = sedeRepository.findSedeByEmpresaAndId(sede.getIdSede(), rt.getEmpresaTransporte().getRazonSocial());
		if(os.isEmpty())
			return false;
		else
			return true;
	}
	
	private boolean perteneceAlUsuario(Usuario u, Vehiculo vehiculo){
		RepresentanteTransporte rt = rtr.findById(u.getIdUsuario()).get();
		Optional<Vehiculo> ov = vehiculoRepository.findById(vehiculo.getPlaca());
		if(ov.isEmpty())
			return false;
		else
			return perteneceAlUsuario(u, ov.get().getSede());
		
	}
	
	private List<Vehiculo> findTransportistasByRepresentante(Usuario usuario) {
		RepresentanteTransporte rt = rtr.findById(usuario.getIdUsuario()).get();
		List<SedeDTO> ls = sedeRepository.findAllSedesByEmpresaTransporte(rt.getEmpresaTransporte().getRazonSocial());
		List<Integer> idSedes = new ArrayList<>();
		for (SedeDTO sede : ls) {
			idSedes.add(sede.getIdSede());
		}
		return vehiculoRepository.findAllVehiculosBySedes(idSedes);
	}
	
	private Vehiculo setTipoVehiculo(Vehiculo vehiculo){
		if (vehiculo instanceof CamionUnitario)
			vehiculo.setTipo(TipoVehiculo.CAMION_UNITARIO);
		else
			vehiculo.setTipo(TipoVehiculo.TRACTOCAMION);
		return vehiculo;
	}
}
