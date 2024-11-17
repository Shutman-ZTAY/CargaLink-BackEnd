package com.ipn.mx.model.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
		assertEquals(exist, true);
	}
	
	@Test
	public void existByClienteAndIdCaseNotFound() {
		boolean exist = ofertaRepository.existByClienteAndId(testOferta.getIdOferta(), "idIncorrecto");
		assertEquals(exist, false);
	}
	
	@Test
	public void findByIdTransportistaCaseFound() {
		Transportista t = testOferta.getRecursos().get(0).getTransportista();
		Oferta query = ofertaRepository.findByIdTransportista(t.getIdUsuario()).get();
		assertEquals(testOferta, query);
	}
	
	@Test
	public void findByIdTransportistaCaseNotFound() {
		Oferta query = ofertaRepository.findByIdTransportista("test").orElse(null);
		assertEquals(null, query);
	}
}
