package com.ipn.mx.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.VectorEmpresa;

public interface VectorEmpresaRepository extends JpaRepository<VectorEmpresa, Integer> {

	@Query("SELECT new VectorEmpresa("
			+ "v.idVector, v.empresa.razonSocial, v.tipoEmpresa, v.puntualidad, v.estadoCarga, v.precio, v.atencion, v.clasificacionComentario) "
			+ "FROM VectorEmpresa v WHERE v.empresa.razonSocial= :razonSocial")
	Optional<VectorEmpresa> findByIdEmpresa(@Param("razonSocial")String razonSocial);
}
