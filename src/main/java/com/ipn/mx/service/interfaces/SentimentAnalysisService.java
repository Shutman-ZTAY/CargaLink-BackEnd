package com.ipn.mx.service.interfaces;

import com.ipn.mx.model.entity.Calificacion;

public interface SentimentAnalysisService {

	public void init();
	public Calificacion setScores(Calificacion calificacion);
	
}
