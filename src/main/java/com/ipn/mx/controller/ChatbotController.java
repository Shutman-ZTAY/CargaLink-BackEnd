package com.ipn.mx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ipn.mx.model.dto.ChatbotResponse;
import com.ipn.mx.model.dto.UseChatbot;
import com.ipn.mx.service.interfaces.IAApiService;

@RestController
@RequestMapping("/usuario")
public class ChatbotController {
	
	@Autowired
	private IAApiService iAApiService;
	
	@PostMapping("/chatbot")
	public ResponseEntity<?> viewOfertaById(@RequestBody UseChatbot useChatbot){
		try {				
			ChatbotResponse chatbotResponse = iAApiService.useChatbot(useChatbot.getPregunta());
			return ControllerUtils.okResponse(chatbotResponse);
		} catch (Exception e) {
			return ControllerUtils.exeptionsResponse(e);
		}
	}

}
