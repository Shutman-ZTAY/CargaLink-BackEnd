package com.ipn.mx.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.RepresentanteTransporte;

public interface RepresentanteTransporteRepository extends JpaRepository<RepresentanteTransporte, String> {

	@Query("SELECT CASE WHEN (COUNT(rt) > 0) THEN TRUE ELSE FALSE END FROM RepresentanteTransporte rt "
			+ "JOIN rt.postulaciones p "
			+ "WHERE p.oferta.idOferta = :idOferta")
	boolean existByOferta(
	        @Param("idOferta") Integer idOferta);
}
