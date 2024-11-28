package com.ipn.mx.model.repository;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.ipn.mx.model.entity.RepresentanteTransporte;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepresentanteTransporteRepositoryTest {
	
	@Autowired
	private RepresentanteTransporteRepository rtr;
	private RepresentanteTransporte testRt;
	
	@BeforeEach
	void init() {
		testRt = rtr.findById("US017").get();
	}
	
	@Test
	public void existByOfertaCaseFound() {
		boolean exist = rtr.existByOferta(testRt.getPostulaciones().get(0).getOferta().getIdOferta());
		assertTrue(exist);
	}
	
	@Test
	public void existByOfertaCaseNotFound() {
		boolean exist = rtr.existByOferta(21);
		assertFalse(exist);
	}
	
}
