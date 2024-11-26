package com.ipn.mx.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipn.mx.model.dto.PreferenciasEmpresas;
import com.ipn.mx.service.interfaces.SortApiService;

@SpringBootTest
@TestPropertySource(properties = "api.python.ia=http://localhost:5000")
public class SortApiServiceTest {
	
	@Autowired
	private SortApiService sortService;
	
	private String PATH_FILE = "src/test/json/ordenamientoPrueba.json"; 
	
	@Test
	public void testApi() throws StreamReadException, DatabindException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		PreferenciasEmpresas pe = objectMapper.readValue(new File(PATH_FILE), PreferenciasEmpresas.class);
		boolean funciona = false;
		List<String> recomendations = sortService.getRecomendations(pe);
		if (recomendations != null) {
			funciona = true;
			System.out.println(recomendations);
		}
		assertTrue(funciona);
	}

}
