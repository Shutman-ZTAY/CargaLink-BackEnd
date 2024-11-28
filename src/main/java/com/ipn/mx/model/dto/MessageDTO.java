package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
	private String contenido;
	private Usuario usuario;
}
