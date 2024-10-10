package com.ipn.mx.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.ipn.mx.model.dto.UsuarioSeguro;
import com.ipn.mx.model.entity.Usuario;

public interface UsuarioService {
	
	public Usuario saveUsuario(Usuario usuario);
	public Optional<Usuario> findUsuarioById(String id);
	public Optional<UsuarioSeguro> findUsuarioSeguroById(String id);
	public Optional<Usuario> findUsuarioByCorreo(String correo);
	public Optional<UsuarioSeguro> findUsuarioSeguroByCorreo(String correo);
	public List<Usuario> findAllUsuarios();
	public void deleteUsuarioById(String id);
	public void deleteUsuarioByCorreo(String correo);
	
}
