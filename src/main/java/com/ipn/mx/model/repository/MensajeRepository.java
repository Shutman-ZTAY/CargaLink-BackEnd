package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ipn.mx.model.entity.Chat;
import com.ipn.mx.model.entity.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer>{
	List<Mensaje> findByChatOrderByFechaAsc(Chat chat);
}

