package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.dto.CalificacionDTO;
import com.ipn.mx.model.entity.Calificacion;

public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {

	@Query("SELECT new com.ipn.mx.model.dto.CalificacionDTO(c.idCalificacion, c.puntualidad, "
	        + "c.estadoCarga, c.precio, c.atencion, c.comentario, c.promedio) "
	        + "FROM Calificacion c "
	        + "JOIN c.oferta.postulaciones p "
	        + "WHERE p.representanteTransporte.idUsuario = :idRepresentanteTransporte")
	List<CalificacionDTO> findAllCalificacionesByRepTransporte(
			@Param("idRepresentanteTransporte")String idRepresentanteTransporte);
	
	@Query("SELECT COUNT(c) FROM Calificacion c WHERE "
			+ "c.oferta.representanteCliente.idUsuario = :idUsuario "
			+ "AND c.clasificacionComentario > 0.25")
	Long countByRepresentanteCliente(@Param("idUsuario") String idUsuario);

	@Query("SELECT COUNT(c) FROM Calificacion c "
			+ "JOIN c.oferta.postulaciones p "
			+ "WHERE p.representanteTransporte.idUsuario = :idRepresentanteTransporte")
	Long countByRepresentanteTransporte(@Param("idRepresentanteTransporte") String idUsuario);

}
