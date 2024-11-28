package com.ipn.mx.service.implementation;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.ipn.mx.exeptions.AnalysisExeption;
import com.ipn.mx.model.entity.Calificacion;
import com.ipn.mx.service.interfaces.SentimentAnalysisService;

import jakarta.annotation.PostConstruct;

@Service
public class SentimentAnalysisServiceImpl implements SentimentAnalysisService {

	private LanguageServiceClient language;

	@Override
	@PostConstruct
	public void init() {
		try {
			this.language = LanguageServiceClient.create();
		} catch (Exception e) {
			System.err.println("Error inicializando LanguageServiceClient: " + e.getMessage());
	        this.language = null;
		}
	}
	
	@Override
	public Calificacion setScores(Calificacion calificacion) {
		if (this.language == null) 
	        throw new AnalysisExeption("Servicio de análisis no está disponible");
		
		Document doc = Document.newBuilder()
				.setContent(calificacion.getComentario())
				.setType(Type.PLAIN_TEXT)
				.build();
		Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
		calificacion.setClasificacionComentario(BigDecimal.valueOf(sentiment.getScore()));
		calificacion.setIntencidadComentario(BigDecimal.valueOf(sentiment.getMagnitude()));
		return calificacion;
		
	}

}
