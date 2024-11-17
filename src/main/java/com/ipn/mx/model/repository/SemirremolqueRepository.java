package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Semirremolque;
import com.ipn.mx.model.enumerated.EstatusVehiculo;

import jakarta.transaction.Transactional;

public interface SemirremolqueRepository extends JpaRepository<Semirremolque, Integer> {

	@Query("SELECT s FROM Semirremolque s WHERE s.sede.idSede IN :sedes")
	List<Semirremolque> findAllSemirremolquesBySedes(@Param("sedes") List<Integer> sedes);

	@Modifying
	@Transactional
	@Query("UPDATE Semirremolque s "
	       +	"SET s.estatus = :estatus "
	       +	"WHERE s.idSemirremolque = :idSemirremolque")
	void updateEstatusSemirremolque(
			@Param("idSemirremolque") Integer idSemirremolque, 
			@Param("estatus") EstatusVehiculo estatus);
	
}
