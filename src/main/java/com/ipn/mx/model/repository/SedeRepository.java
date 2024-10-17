package com.ipn.mx.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.dto.SedeDTO;
import com.ipn.mx.model.entity.Sede;

import jakarta.transaction.Transactional;

public interface SedeRepository extends JpaRepository<Sede, Integer> {

	@Query("SELECT new com.ipn.mx.model.dto.SedeUpd(s.idSede, s.nombre, s.direccion) "
			+ "FROM Sede s WHERE s.empresaTransporte.razonSocial= :razonSocial")
	List<SedeDTO> findAllSedesByEmpresaTransporte(@Param("razonSocial")String razonSocial);

	@Query("SELECT new com.ipn.mx.model.dto.SedeUpd(s.idSede, s.nombre, s.direccion) "
			+ "FROM Sede s WHERE s.empresaTransporte.razonSocial= :razonSocial AND s.idSede= :idSede")
	Optional<SedeDTO> findByEmpresaAndId(
			@Param("idSede")Integer idSede,
			@Param("razonSocial")String razonSocial);
	
	@Transactional
	@Modifying
	@Query("UPDATE Sede s "
			+ "SET s.nombre = :newNombre, s.direccion = :newDireccion "
			+ "WHERE s.idSede = :idSede")
	void updateSede(
			@Param("idSede") Integer idSede,
			@Param("newNombre") String newNombre,
			@Param("newDireccion") String newDireccion);
	
}
