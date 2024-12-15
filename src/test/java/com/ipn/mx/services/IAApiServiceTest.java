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
import com.ipn.mx.model.dto.ChatbotResponse;
import com.ipn.mx.model.dto.PreferenciasEmpresas;
import com.ipn.mx.service.interfaces.IAApiService;

@SpringBootTest
@TestPropertySource(properties = {
		"spring.mail.host=smtp.gmail.com"
		,"spring.mail.port=587"
		,"spring.mail.username=ismaelestanislaoc@gmail.com"
		,"spring.mail.password=**** **** **** ****"
		,"spring.mail.protocol=smtp"
		,"spring.mail.properties.mail.smtp.auth=true"
		,"spring.mail.properties.mail.smtp.starttls.enable=true"
		,"api.python.ia=http://localhost:5000"})
public class IAApiServiceTest {
	
	@Autowired
	private IAApiService iAApiService;
	
	private String PATH_FILE = "src/test/json/ordenamientoPrueba.json"; 
	
	@Test
	public void testSort() throws StreamReadException, DatabindException, IOException{
		ObjectMapper objectMapper = new ObjectMapper();
		PreferenciasEmpresas pe = objectMapper.readValue(new File(PATH_FILE), PreferenciasEmpresas.class);
		boolean funciona = false;
		List<String> recomendations = iAApiService.getRecomendations(pe);
		if (recomendations != null) {
			funciona = true;
			System.out.println(recomendations);
		}
		assertTrue(funciona);
	}
	
	@Test
	public void testChatbot() throws StreamReadException, DatabindException, IOException{
		String question = "¿Es posible incluir información adicional que no esté contemplada en los campos predefinidos?";
		boolean funciona = false;
		ChatbotResponse response = iAApiService.useChatbot(question);
		if (response != null) {
			funciona = true;
			System.out.println(response.getRespuesta());
		}
		assertTrue(funciona);
	}

}
