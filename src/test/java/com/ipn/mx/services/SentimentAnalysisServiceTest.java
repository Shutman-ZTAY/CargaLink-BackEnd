package com.ipn.mx.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.ipn.mx.model.entity.Calificacion;
import com.ipn.mx.service.interfaces.SentimentAnalysisService;

public class SentimentAnalysisServiceTest {

	@Autowired
	private SentimentAnalysisService analysisService;
	
	@Test
	public void usuarioEmpresaCaseTrue(){
		String comentario = "Los audífonos ofrecen una excelente relación calidadprecio, destacando por su buena "
				+ "cancelación de ruido y sonido. Además, cuentan con una aplicación que permite personalizar el ecualizador, "
				+ "lo que mejora significativamente la experiencia auditiva.";
		Calificacion c = Calificacion.builder().comentario(comentario).build();
		c = analysisService.setScores(c);
		boolean funciona = false;
		if (c.getClasificacionComentario() != null && c.getIntencidadComentario() != null) {
			System.out.println("Comentario: " + comentario);
			System.out.println("Clasificacion: " + c.getIntencidadComentario());
			System.out.println("Intencidad: " + c.getIntencidadComentario());
			funciona = true;
		}
		
		assertTrue(funciona);
	}
}
