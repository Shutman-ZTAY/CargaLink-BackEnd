package com.ipn.mx.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ipn.mx.model.entity.Chat;
import com.ipn.mx.model.entity.Usuario;

public interface ChatRepository extends JpaRepository<Chat, Integer>{
	@Query("SELECT c FROM Chat c WHERE (c.usuario1 = :usuario1 AND c.usuario2 = :usuario2) OR (c.usuario1 = :usuario2 AND c.usuario2 = :usuario1)")
	Optional<Chat> findByUsuario1AndUsuario2OrUsuario2AndUsuario1(Usuario usuario1, Usuario usuario2);
	
	@Query("SELECT c FROM Chat c WHERE c.usuario1 = :usuario OR c.usuario2 = :usuario")
	List<Chat> findByUsuario(Usuario usuario);
}

