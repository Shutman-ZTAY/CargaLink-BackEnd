package com.ipn.mx.model.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Recurso;

import jakarta.transaction.Transactional;

public interface RecursoRepository extends JpaRepository<Recurso, Integer> {

	@Query("SELECT CASE WHEN COUNT(r) = 0 THEN TRUE ELSE FALSE END "
			+ "FROM Recurso r "
			+ "WHERE r.oferta.idOferta = :idOferta "
			+ "AND r.estatus != com.ipn.mx.model.enumerated.EstatusRecurso.ENTREGADO")
	boolean areAllRecursosEntregados(@Param("idOferta") Integer idOferta);

	
	@Transactional
	@Modifying
	@Query("DELETE FROM Recurso r WHERE r.oferta.idOferta = :idOferta")
	void deleteByidOferta(@Param("idOferta") Integer idOferta);

	@Query("SELECT r FROM Recurso r "
			+ "WHERE r.oferta.idOferta = :idOferta "
			+ "AND r.transportista.idUsuario = :idUsuario")
	Optional<Recurso> findByTransportistaAndOferta(
			@Param("idUsuario") String idUsuario, 
			@Param("idOferta") Integer idOferta);

}
