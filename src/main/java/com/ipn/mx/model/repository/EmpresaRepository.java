package com.ipn.mx.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Empresa;

import jakarta.transaction.Transactional;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {
	@Modifying
	@Transactional
	@Query("UPDATE Empresa e " +
	       "SET e.nombreComercial = :nombreComercial, e.rfc = :rfc, e.direccion = :direccion, e.descripcion = :descripcion " +
	       "WHERE e.razonSocial = :razonSocial")
	void updateEmpresaCliente(
	    @Param("razonSocial") String razonSocial,
	    @Param("nombreComercial") String nombreComercial,
	    @Param("rfc") String rfc,
	    @Param("direccion") String direccion,
	    @Param("descripcion") String descripcion
	);
}
