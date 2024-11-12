package com.ipn.mx.model.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.enumerated.EstatusOferta;

import jakarta.transaction.Transactional;

public interface OfertaRepository extends JpaRepository<Oferta, Integer> {

	@Query("SELECT o FROM Oferta o WHERE o.representanteCliente.idUsuario = :idReprCliente")
	List<Oferta> findAllOfertasByReprCliente(@Param("idReprCliente") String idReprCliente);
	
	@Query("SELECT CASE WHEN (COUNT(o) > 0) THEN TRUE ELSE FALSE END FROM Oferta o "
			+ "WHERE o.representanteCliente.idUsuario = :idReprCliente "
			+ "AND o.idOferta = :idOferta")
	boolean existByClienteAndId(
			@Param("idOferta") Integer idOferta,
			@Param("idReprCliente")String idUsuario);
	
	@Query("SELECT o FROM Oferta o WHERE o.contrato = :contrato")
	Optional<Oferta> findByContrato(
			@Param("contrato")String contrato);

	@Query("SELECT o FROM Oferta o WHERE o.estatus = OFERTA")
	List<Oferta> findAllOfertasDisponibles();

	@Transactional
	@Modifying
	@Query("UPDATE Oferta o "
			+ "SET o.estatus = :estatus "
			+ "WHERE o.idOferta = :idOferta")
	void updateEstatusOferta(
			@Param("idOferta") Integer idOferta, 
			@Param("estatus") EstatusOferta estatus);
	
	@Transactional
	@Modifying
	@Query("UPDATE Oferta o "
			+ "SET o.contrato = :contrato "
			+ "WHERE o.idOferta = :idOferta")
	void updateContrato(
			@Param("idOferta") Integer idOferta, 
			@Param("contrato") String contrato);

	@Query("SELECT o FROM Oferta o "
			+ "JOIN o.recursos r "
			+ "WHERE r.transportista.idUsuario = :idTransportista "
			+ "AND (o.estatus = EstatusOferta.RECOGIENDO OR o.estatus = EstatusOferta.EMBARCANDO "
			+ "OR o.estatus = EstatusOferta.EN_CAMINO OR o.estatus = EstatusOferta.PROBLEMA "
			+ "OR o.estatus = EstatusOferta.ENTREGADO)")
	Optional<Oferta> findByIdTransportista(
	        @Param("idTransportista") String idTransportista);

	@Transactional
	@Modifying
	@Query("UPDATE Oferta o "
			+ "SET o.tokenViaje = :token "
			+ "WHERE o.idOferta = :idOferta")
	void updateToken(
			@Param("idOferta") Integer idOferta, 
			@Param("token") String token);
	
}
