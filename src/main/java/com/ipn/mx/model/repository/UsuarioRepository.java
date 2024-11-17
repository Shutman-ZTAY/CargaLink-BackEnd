package com.ipn.mx.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Usuario;

import jakarta.transaction.Transactional;

public interface UsuarioRepository extends JpaRepository<Usuario, String> {

	@Query("SELECT u FROM Usuario u WHERE u.correo = :correo")
    Optional<Usuario> findUsuarioByCorreo(@Param("correo") String correo);
	
	@Transactional
	@Query("DELETE FROM Usuario u WHERE u.correo = :correo")
    void deleteUsuarioByCorreo(@Param("correo") String correo);
}
