package com.ipn.mx.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Oferta;

import jakarta.transaction.Transactional;

public interface OfertaRepository extends JpaRepository<Oferta, Integer> {

	@Query("SELECT o FROM Oferta o WHERE o.representanteCliente.idUsuario = :idReprCliente")
	List<Oferta> findAllOfertasByReprCliente(@Param("idReprCliente") String idReprCliente);

	@Query("SELECT o FROM Oferta o WHERE o.representanteCliente.idUsuario = :idReprCliente AND o.idOferta = :idOferta")
	Optional<Oferta> findOfertaByClienteAndId(
			@Param("idOferta") Integer idOferta,
			@Param("idReprCliente")String idUsuario);

	@Query("SELECT o FROM Oferta o WHERE o.estatus = OFERTA")
	List<Oferta> findAllOfertasDisponibles();

	@Transactional
	@Modifying
	@Query("UPDATE Oferta o "
			+ "SET o.estatus = :estatus "
			+ "WHERE o.idOferta = :idOferta")
	void updateEstatusOferta(
			@Param("idOferta") Integer idOferta, 
			@Param("estatus") String estatus);
	
}
