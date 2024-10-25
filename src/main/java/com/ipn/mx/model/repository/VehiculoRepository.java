package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Vehiculo;
import com.ipn.mx.model.enumerated.EstatusVehiculo;

import jakarta.transaction.Transactional;

public interface VehiculoRepository extends JpaRepository<Vehiculo, String> {

	@Query("SELECT v FROM Vehiculo v WHERE v.sede.idSede IN :sedes")
	List<Vehiculo> findAllVehiculosBySedes(@Param("sedes") List<Integer> sedes);

	@Modifying
	@Transactional
	@Query("UPDATE Vehiculo v "
	       + "SET v.estatus = :estatus "
	       + "WHERE v.placa = :placa")
	void updateEstatusVehiculo(
			@Param("placa") String placa, 
			@Param("enViaje") EstatusVehiculo enViaje);
}
