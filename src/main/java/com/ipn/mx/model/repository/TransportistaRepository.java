package com.ipn.mx.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.dto.TransportistaSeguro;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.enumerated.CategoriaTransportista;
import com.ipn.mx.model.enumerated.EstatusTransportista;

import jakarta.transaction.Transactional;

public interface TransportistaRepository extends JpaRepository<Transportista, String> {
	
	@Modifying
	@Transactional
	@Query("UPDATE Transportista t " +
	       "SET t.password = :password, t.telefono = :telefono, t.estatusTransportista = :estatusTransportista " +
	       "WHERE t.idUsuario = :idUsuario")
	void updateTransportista(
	    @Param("idUsuario") String idUsuario,
	    @Param("password") String password,
	    @Param("telefono") String telefono,
	    @Param("estatusTransportista") EstatusTransportista estatusTransportista
	);
	
	@Modifying
	@Transactional
	@Query("UPDATE Transportista t " +
	       "SET t.experiencia = :experiencia, t.categoria = :categoria, t.sede.idSede = :sede " +
	       "WHERE t.idUsuario = :idUsuario")
	void updateTransportistaFromRepresentante(
			@Param("idUsuario") String idUsuario, 
			@Param("experiencia") Integer experiencia,
			@Param("categoria") CategoriaTransportista categoria, 
			@Param("sede") Integer sede
			);


	@Query("SELECT new com.ipn.mx.model.dto.TransportistaSeguro"
			+ "(t.idUsuario, t.nombre, t.primerApellido, t.segundoApellido, "
			+ "t.correo, t.telefono, t.rol, t.experiencia, t.categoria, "
			+ "t.estatusTransportista, t.sede) "
	         + "FROM Transportista t WHERE t.sede.idSede IN :sedes")
	List<TransportistaSeguro> findAllTransportistasByAllSedes(@Param("sedes") List<Integer> sedes);

}
