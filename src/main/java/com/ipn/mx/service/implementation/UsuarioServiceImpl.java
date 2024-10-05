package com.ipn.mx.service.implementation;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipn.mx.model.dto.UsuarioSeguro;
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
			UsuarioSeguro us = UsuarioSeguro
					.builder()
					.idUsuario(u.getIdUsuario())
					.correo(u.getCorreo())
					.nombre(u.getNombre())
					.primerApellido(u.getPrimerApellido())
					.segundoApellido(u.getSegundoApellido())
					.telefono(u.getTelefono())
					.rol(u.getRol())
					.build();
			return Optional.of(us);
		}
	}
	
}
