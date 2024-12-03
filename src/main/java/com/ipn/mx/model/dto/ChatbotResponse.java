package com.ipn.mx.model.dto;

import com.google.auto.value.AutoValue.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatbotResponse {

	private String pregunta;
	private String respuesta;
	private boolean excedeUmbral;
	
}
