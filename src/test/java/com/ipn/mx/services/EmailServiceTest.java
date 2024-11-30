package com.ipn.mx.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import com.ipn.mx.service.interfaces.EmailService;

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
public class EmailServiceTest {
	
	@Autowired
	private EmailService emailService;
	
	@Test
	public void sendTest(){
		boolean funciona = true;
		try {
			emailService.sendBasicEmail("alex.cas.dos@gmail.com", "Prueba", "Esto es una prueba");
		} catch (Exception e) {
			e.printStackTrace();
			funciona = false;
		}
		
		assertTrue(funciona);
	}
	

}
