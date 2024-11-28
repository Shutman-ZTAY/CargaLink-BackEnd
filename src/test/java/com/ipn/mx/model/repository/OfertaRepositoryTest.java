package com.ipn.mx.model.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.entity.Transportista;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OfertaRepositoryTest {

	@Autowired
	private OfertaRepository ofertaRepository;
	
	private Oferta testOferta;
	
	@BeforeEach
	void init() {
		Oferta o = ofertaRepository.findById(13).get();
		testOferta = o;
	}
	
	@Test
	public void existByClienteAndIdCaseFound() {
		boolean exist = ofertaRepository.existByClienteAndId(testOferta.getIdOferta(), testOferta.getRepresentanteCliente().getIdUsuario());
		assertTrue(exist);
	}
	
	@Test
	public void existByClienteAndIdCaseNotFound() {
		boolean exist = ofertaRepository.existByClienteAndId(testOferta.getIdOferta(), "idIncorrecto");
		assertFalse(exist);
	}
	
	@Test
	public void findByIdTransportistaCaseFound() {
		Transportista t = testOferta.getRecursos().get(0).getTransportista();
		Oferta query = ofertaRepository.findByIdTransportista(t.getIdUsuario()).get();
		boolean asserts = false;
		if (testOferta.equals(query)) {
			asserts = true;
		}
		assertTrue(asserts);
	}
	
	@Test
	public void findByIdTransportistaCaseNotFound() {
		Oferta query = ofertaRepository.findByIdTransportista("test").orElse(null);
		boolean found = false;
		if (testOferta.equals(query)) {
			found = true;
		}
		assertFalse(found);
	}
}
