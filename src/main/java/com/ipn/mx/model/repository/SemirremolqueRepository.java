package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Semirremolque;

public interface SemirremolqueRepository extends JpaRepository<Semirremolque, Integer> {

	@Query("SELECT s FROM Semirremolque s WHERE s.sede.idSede IN :sedes")
	List<Semirremolque> findAllSemirremolquesBySedes(@Param("sedes") List<Integer> sedes);
	
}
