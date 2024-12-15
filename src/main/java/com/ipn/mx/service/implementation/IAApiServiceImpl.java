package com.ipn.mx.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipn.mx.model.dto.ChatbotResponse;
import com.ipn.mx.model.dto.PreferenciasEmpresas;
import com.ipn.mx.model.dto.UseChatbot;
import com.ipn.mx.service.interfaces.IAApiService;

@Service
public class IAApiServiceImpl implements IAApiService {
	
	@Value("${api.python.ia}")
	private String API_URL;

	@Override
	public List<String> getRecomendations(PreferenciasEmpresas preferenciasEmpresas) throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(preferenciasEmpresas);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<List<String>> response = restTemplate.exchange(
		    API_URL + "/reordenar",
		    HttpMethod.POST,
		    new HttpEntity<>(json, headers),
		    new ParameterizedTypeReference<List<String>>() {}
		);

		List<String> idEmpresas = response.getBody();
		return idEmpresas;
	}
	
	@Override
	public ChatbotResponse useChatbot(String question) throws JsonProcessingException {
		UseChatbot requestBody = new UseChatbot(question); 
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(requestBody);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ChatbotResponse> response = restTemplate.exchange(
		    API_URL + "/chatbot",
		    HttpMethod.POST,
		    new HttpEntity<>(json, headers),
		    ChatbotResponse.class
		);

		ChatbotResponse chatbotResponse = response.getBody();
		return chatbotResponse;
	}

}
