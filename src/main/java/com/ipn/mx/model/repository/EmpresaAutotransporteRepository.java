package com.ipn.mx.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.EmpresaAutotransporte;

import jakarta.transaction.Transactional;

public interface EmpresaAutotransporteRepository extends JpaRepository<EmpresaAutotransporte, String> {
	
	@Modifying
	@Transactional
	@Query("UPDATE EmpresaAutotransporte ea " +
	       "SET ea.nombreComercial = :nombreComercial, ea.rfc = :rfc, ea.direccion = :direccion, ea.descripcion = :descripcion " +
	       "WHERE ea.razonSocial = :razonSocial")
	void updateEmpresaTransporte(
	    @Param("razonSocial") String razonSocial,
	    @Param("nombreComercial") String nombreComercial,
	    @Param("rfc") String rfc,
	    @Param("direccion") String direccion,
	    @Param("descripcion") String descripcion
	);
	
	@Query("SELECT ea FROM EmpresaAutotransporte ea WHERE ea.documentoFiscal = :documentoFiscal")
	Optional<EmpresaAutotransporte> findByDocumentoFiscal(
			@Param("documentoFiscal")String documentoFiscal);
}
