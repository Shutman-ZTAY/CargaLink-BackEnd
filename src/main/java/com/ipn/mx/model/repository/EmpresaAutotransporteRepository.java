package com.ipn.mx.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.EmpresaAutotransporte;

public interface EmpresaAutotransporteRepository extends JpaRepository<EmpresaAutotransporte, String> {

	@Query("SELECT ea FROM EmpresaAutotransporte ea WHERE ea.documentoFiscal = :documentoFiscal")
	Optional<EmpresaAutotransporte> findByDocumentoFiscal(
			@Param("documentoFiscal")String documentoFiscal);
}
