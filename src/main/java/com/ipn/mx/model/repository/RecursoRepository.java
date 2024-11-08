package com.ipn.mx.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Recurso;

import jakarta.transaction.Transactional;

public interface RecursoRepository extends JpaRepository<Recurso, Integer> {

	@Transactional
	@Modifying
	@Query("DELETE FROM Recurso r WHERE r.oferta.idOferta = :idOferta")
	void deleteByidOferta(@Param("idOferta") Integer idOferta);

}
