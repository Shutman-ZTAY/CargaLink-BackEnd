package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ipn.mx.model.dto.PostulacionDTO;
import com.ipn.mx.model.entity.Postulacion;

import jakarta.transaction.Transactional;

@Repository
public interface PostulacionRepository extends JpaRepository<Postulacion, Integer>{

	@Query("SELECT new com.ipn.mx.model.dto.PostulacionDTO(p.idPostulacion, p.oferta, p.representanteTransporte, p.precioPreeliminar) "
			+ "FROM Postulacion p WHERE p.representanteTransporte.idUsuario= :idRepresentanteTransporte")
	List<PostulacionDTO> findAllPostulacionesByReprTransporte(@Param("idRepresentanteTransporte") String idRepresentanteTransporte);

	@Transactional
	@Query("DELETE FROM Postulacion p WHERE "
			+ "p.oferta.idOferta = :idOferta "
			+ "AND "
			+ "p.representanteTransporte.idUsuario <> :idUsuario")
	void deletePostulacionesNoAceptadas(
			@Param("idOferta") Integer idOferta, 
			@Param("idUsuario") String idUsuario);
	
}