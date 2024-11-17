package com.ipn.mx.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatSeguro {
	private Integer idChat;
	private UsuarioSeguroMensaje usuario1;
	private UsuarioSeguroMensaje usuario2;
}
