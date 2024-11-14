package com.ipn.mx.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipn.mx.model.entity.Chat;
import com.ipn.mx.model.entity.Usuario;

public interface ChatRepository extends JpaRepository<Chat, Integer>{
	Optional<Chat> findByUsuario1AndUsuario2OrUsuario2AndUsuario1(Usuario usuario1, Usuario usuario2, Usuario usuario2Alt, Usuario usuario1Alt);
}

