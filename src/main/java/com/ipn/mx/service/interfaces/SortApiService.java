package com.ipn.mx.service.interfaces;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ipn.mx.model.dto.PreferenciasEmpresas;

public interface SortApiService {
	
	public List<String> getRecomendations(PreferenciasEmpresas preferenciasEmpresas) throws JsonProcessingException;

}
