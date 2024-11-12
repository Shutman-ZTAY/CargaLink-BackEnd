package com.ipn.mx.model.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.EstatusRepTrans;
import com.ipn.mx.model.enumerated.RolUsuario;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UsuarioRepositioryTest {

	@Autowired
	UsuarioRepository usuarioRepository;
	
	@BeforeEach
	void init() {
		RepresentanteTransporte rt = RepresentanteTransporte.builder()
				.idUsuario("RFCTEST")
				.nombre("Test")
				.primerApellido("Prueba")
				.segundoApellido("Prueba")
				.correo("test@gmail.com")
				.password("1234")
				.telefono("5123648")
				.rol(RolUsuario.REPRESENTANTE_TRANSPORTE)
				.estatusRepTrans(EstatusRepTrans.VALIDO)
				.empresaTransporte(null)
				.build();
		usuarioRepository.save(rt);
	}
	
	@Test
	public void findByCorreoTestCaseFound() {
		Optional<Usuario> ou = usuarioRepository.findUsuarioByCorreo("test@gmail.com");
		assertEquals(ou.get().getIdUsuario(), "RFCTEST");
	}
	
	@Test
	public void findByCorreoTestCaseNotFound() {
		Optional<Usuario> ou = usuarioRepository.findUsuarioByCorreo("prueba@gmail.com");
		assertEquals(ou.orElse(null), null);
	}
}
