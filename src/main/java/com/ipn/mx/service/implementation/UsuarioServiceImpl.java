package com.ipn.mx.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.rsocket.server.RSocketServer.Transport;
import org.springframework.stereotype.Service;

import com.ipn.mx.model.dto.RepresentanteClienteSeguro;
import com.ipn.mx.model.dto.RepresentanteTransporteSeguro;
import com.ipn.mx.model.dto.TransportistaSeguro;
import com.ipn.mx.model.dto.UsuarioSeguro;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.repository.UsuarioRepository;
import com.ipn.mx.service.interfaces.UsuarioService;

@Service
public class UsuarioServiceImpl implements UsuarioService {
	
	@Autowired
	private UsuarioRepository ur;

	@Override
	public Usuario saveUsuario(Usuario usuario) {
		return ur.save(usuario);
	}

	@Override
	public Optional<Usuario> findUsuarioById(String id) {
		return ur.findById(id);
	}

	@Override
	public Optional<UsuarioSeguro> findUsuarioSeguroById(String id) {
		return usuarioToUsuarioSeguro(ur.findById(id));
	}

	@Override
	public Optional<Usuario> findUsuarioByCorreo(String correo) {
		return ur.findUsuarioByCorreo(correo);
	}

	@Override
	public Optional<UsuarioSeguro> findUsuarioSeguroByCorreo(String correo) {
		return usuarioToUsuarioSeguro(ur.findUsuarioByCorreo(correo));
	}

	@Override
	public List<Usuario> findAllUsuarios() {
		return ur.findAll();
	}

	@Override
	public void deleteUsuarioById(String id) {
		ur.deleteById(id);
	}
	
	@Override
	public void deleteUsuarioByCorreo(String correo) {
		ur.deleteUsuarioByCorreo(correo);
	}
	
	private Optional<UsuarioSeguro> usuarioToUsuarioSeguro(Optional<Usuario> ou){
		if (ou.isEmpty()) {
			return Optional.empty();
		} else {
			Usuario u = ou.get();
			UsuarioSeguro us = UsuarioSeguro.usuarioToUsuarioSeguro(u); 
			if (u instanceof Usuario) {
				return Optional.of(us);
			}else if (u instanceof Transportista) {
				TransportistaSeguro ts = TransportistaSeguro.usuarioSeguroToTransportistaSeguro(us);
				Transportista t = (Transportista) u; 
				ts.setExperiencia(ts.getExperiencia());
				ts.setCategoria(t.getCategoria());
				ts.setEstatusTransportista(t.getEstatusTransportista());
				ts.setSede(t.getSede());
				return Optional.of(ts);
			}else if (u instanceof RepresentanteCliente) {
				RepresentanteClienteSeguro rcs = RepresentanteClienteSeguro.usuarioSeguroToRepresentanteClienteSeguro(us);
				RepresentanteCliente rc = (RepresentanteCliente) u;
				rcs.setEmpresaCliente(rc.getEmpresaCliente());
				return Optional.of(rcs);
			} else {
				RepresentanteTransporteSeguro rts = RepresentanteTransporteSeguro.usuarioSeguroToRepresentanteTransporteSeguro(us);
				RepresentanteTransporte rt = (RepresentanteTransporte) u;
				rts.setEstatusRepTrans(rt.getEstatusRepTrans());
				rts.setEmpresaTransporte(rt.getEmpresaTransporte());
				return Optional.of(rts);
			}
		}
	}
	
}
