package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.enumerated.EstatusTransportista;

import jakarta.transaction.Transactional;

public interface RepresentanteTransporteRepository extends JpaRepository<RepresentanteTransporte, String> {
	@Modifying
	@Transactional
	@Query("UPDATE RepresentanteTransporte rt " +
	       "SET rt.password = :password, rt.telefono = :telefono, rt.nombre = :nombre, rt.primerApellido = :primerApellido, rt.segundoApellido = :segundoApellido," +
			"rt.correo = :correo "+
	       "WHERE rt.idUsuario = :idUsuario")
	void updateReptrans(
	    @Param("idUsuario") String idUsuario,
	    @Param("password") String password,
	    @Param("telefono") String telefono,
	    @Param("nombre") String nombre,
	    @Param("primerApellido") String primerApellido,
	    @Param("segundoApellido") String segundoApellido,
	    @Param("correo") String correo
	);
	
	@Query("SELECT rt FROM RepresentanteTransporte rt "
			+ "WHERE rt.estatusRepTrans = EstatusRepTrans.NO_VALIDO")
	List<RepresentanteTransporte> findAllNotValid();
	
	@Query("SELECT CASE WHEN (COUNT(rt) > 0) THEN TRUE ELSE FALSE END FROM RepresentanteTransporte rt "
			+ "JOIN rt.postulaciones p "
			+ "WHERE p.oferta.idOferta = :idOferta")
	boolean existByOferta(
	        @Param("idOferta") Integer idOferta);
}
