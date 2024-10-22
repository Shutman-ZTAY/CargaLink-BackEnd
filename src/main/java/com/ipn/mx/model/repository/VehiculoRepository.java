package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Vehiculo;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

	@Query("SELECT v FROM Vehiculo v WHERE v.sede.idSede IN :sedes")
	List<Vehiculo> findAllVehiculosBySedes(@Param("sedes") List<Integer> sedes);
}
