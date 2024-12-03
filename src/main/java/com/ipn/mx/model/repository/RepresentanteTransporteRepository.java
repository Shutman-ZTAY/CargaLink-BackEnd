package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.enumerated.EstatusRepTrans;

public interface RepresentanteTransporteRepository extends JpaRepository<RepresentanteTransporte, String> {
	
	@Query("SELECT rt FROM RepresentanteTransporte rt "
			+ "WHERE rt.estatus = EstatusRepTrans.VALIDO")
	List<RepresentanteTransporte> findAllNotValid();

	@Query("SELECT CASE WHEN (COUNT(rt) > 0) THEN TRUE ELSE FALSE END FROM RepresentanteTransporte rt "
			+ "JOIN rt.postulaciones p "
			+ "WHERE p.oferta.idOferta = :idOferta")
	boolean existByOferta(
	        @Param("idOferta") Integer idOferta);
}
