package com.ipn.mx.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ipn.mx.model.entity.RepresentanteCliente;

import jakarta.transaction.Transactional;

public interface RepresentanteClienteRepository extends JpaRepository<RepresentanteCliente, String> {
	@Modifying
	@Transactional
	@Query("UPDATE RepresentanteCliente rc " +
	       "SET rc.password = :password, rc.telefono = :telefono, rc.nombre = :nombre, rc.primerApellido = :primerApellido, rc.segundoApellido = :segundoApellido," +
			"rc.correo = :correo "+
	       "WHERE rc.idUsuario = :idUsuario")
	void updateRepcli(
	    @Param("idUsuario") String idUsuario,
	    @Param("password") String password,
	    @Param("telefono") String telefono,
	    @Param("nombre") String nombre,
	    @Param("primerApellido") String primerApellido,
	    @Param("segundoApellido") String segundoApellido,
	    @Param("correo") String correo
	);
}
