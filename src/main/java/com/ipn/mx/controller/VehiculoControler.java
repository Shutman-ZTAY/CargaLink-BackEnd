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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.SedeDTO;
import com.ipn.mx.model.dto.VehiculoDTO;
import com.ipn.mx.model.entity.CamionUnitario;
import com.ipn.mx.model.entity.RepresentanteTransporte;
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
	@Autowired 
	private ControllerUtils controllerUtils;

	// RF07	Gestionar vehículos
	@PostMapping("")
	public ResponseEntity<?> createVehiculo(@RequestBody(required = true) Vehiculo vehiculo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				vehiculo = setTipoVehiculo(vehiculo);
				if (controllerUtils.perteneceAlUsuario(u, vehiculo.getSede()) && !vehiculoRepository.existsById(vehiculo.getPlaca())) {
					vehiculoRepository.save(vehiculo);
					return ControllerUtils.createdResponse();
				} else {
					return ControllerUtils.badRequestResponse();
				}
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else 
			return ControllerUtils.unauthorisedResponse();
	}
	
	// RF07	Gestionar vehículos
	@GetMapping("/{idVehiculo}")
	public ResponseEntity<?> viewVehiculoById(@PathVariable String idVehiculo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				Vehiculo v = vehiculoRepository.findById(idVehiculo).orElseThrow(() -> new NoSuchElementException("No se encontro el vehiculo"));
				if (!controllerUtils.perteneceAlUsuario(u, v))
					return ControllerUtils.unauthorisedResponse();
				return ControllerUtils.okResponse(VehiculoDTO.toVehiculoDTO(v));
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else
			return ControllerUtils.unauthorisedResponse();
	}
	
	// RF07	Gestionar vehículos
	@GetMapping("")
	public ResponseEntity<?> viewAllVehiculos(
			@RequestParam(required = false, name = "idRepresentanteAutotransporte") String idRepresentanteAutotransporte) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				if(u.getRol() == RolUsuario.ADMINISTRADOR)
					u = rtr.findById(idRepresentanteAutotransporte)
					.orElseThrow(() -> new NoSuchElementException("No se encontró el Usuario con ID: " + idRepresentanteAutotransporte));
				List<Vehiculo> lv = findVehiculosByRepresentante(u);
				return ControllerUtils.okResponse(lv);
			} catch (Exception e) {
				return ControllerUtils.exeptionsResponse(e);
			}
		} else
			return ControllerUtils.unauthorisedResponse();
	}
	
	// RF07	Gestionar vehículos
	@PutMapping("/{id}")
	public ResponseEntity<?> updateVehiculo(
			@PathVariable String id, 
			@RequestBody(required = true) Vehiculo vehiculo) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				vehiculo = setTipoVehiculo(vehiculo);
				vehiculo.setPlaca(id);
				if (controllerUtils.perteneceAlUsuario(u, vehiculo)) {
					vehiculo.setPlaca(id);
					vehiculo = getUpdateVehiculo(vehiculo);
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
	
	// RF07	Gestionar vehículos
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteVehiculo(@PathVariable String id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Usuario u = (Usuario) auth.getPrincipal();
		if (ControllerUtils.isAuthorised(auth, RolUsuario.REPRESENTANTE_TRANSPORTE)) {
			try {
				Vehiculo vehiculo = vehiculoRepository.findById(id).orElse(null);
				if (controllerUtils.perteneceAlUsuario(u, vehiculo)) {
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
	
	private List<Vehiculo> findVehiculosByRepresentante(Usuario usuario) {
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
	
	private Vehiculo getUpdateVehiculo(Vehiculo vehiculo) {
		Vehiculo vUpdate = vehiculoRepository.findById(vehiculo.getPlaca()).get();
		if (vehiculo.getPeso() != null) 
			vUpdate.setPeso(vehiculo.getPeso());
		if (vehiculo.getNoEjes() != null) 
			vUpdate.setNoEjes(vehiculo.getNoEjes());
		if (vehiculo.getNoLlantas() != null) 
			vUpdate.setNoLlantas(vehiculo.getNoLlantas());
		if (vehiculo.getLargo() != null) 
			vUpdate.setLargo(vehiculo.getLargo());
		if (vehiculo.getMarca() != null) 
			vUpdate.setMarca(vehiculo.getMarca());
		if (vehiculo.getTipo() != null) 
			vUpdate.setTipo(vehiculo.getTipo());
		if (vehiculo.getEstatus() != null) 
			vUpdate.setEstatus(vehiculo.getEstatus());
		if (vehiculo.getModelo() != null) 
			vUpdate.setModelo(vehiculo.getModelo());
		if (vehiculo.getSede() != null) 
			vUpdate.setSede(vehiculo.getSede());
		
		if (vehiculo instanceof CamionUnitario) {
			CamionUnitario cuUpdate = (CamionUnitario) vUpdate;
			CamionUnitario cuArg = (CamionUnitario) vehiculo;
			if (cuArg.getTipoCamion() != null)
				cuUpdate.setTipoCamion(cuArg.getTipoCamion());
			return cuUpdate;
		}
		
		return vUpdate;
	}
}
