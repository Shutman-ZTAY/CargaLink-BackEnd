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
public class MensajeSeguro {
	private Integer idMensaje;
	private UsuarioSeguroMensaje usuario;
	private Integer idChat;
	private String contenido;
	private LocalDateTime fecha;
}
