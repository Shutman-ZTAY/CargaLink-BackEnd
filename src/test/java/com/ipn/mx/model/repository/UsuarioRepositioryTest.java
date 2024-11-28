package com.ipn.mx.model.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
		boolean found = false;
		if (ou.get().getIdUsuario().equals("RFCTEST")) {
			found = true;
		}
		assertTrue(found);
	}
	
	@Test
	public void findByCorreoTestCaseNotFound() {
		Optional<Usuario> ou = usuarioRepository.findUsuarioByCorreo("prueba@gmail.com");
		boolean found = false;
		if (ou.orElse(null) != null) {
			found = true;
		}
		assertFalse(found);
	}
}
