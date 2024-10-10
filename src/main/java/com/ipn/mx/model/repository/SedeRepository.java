package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Sede;

public interface SedeRepository extends JpaRepository<Sede, Integer> {

	@Query("SELECT s FROM Sede s WHERE s.empresaTransporte.razonSocial= :razonSocial")
	List<Sede> findAllSedesByEmpresaTransporte(@Param("razonSocial")String razonSocial);
	
}
