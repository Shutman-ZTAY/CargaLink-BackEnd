package com.ipn.mx.model.dto;

import com.ipn.mx.model.enumerated.CategoriaTransportista;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTransportistaRepresentante {

	private String idUsuario;
	private Integer experiencia;
	private CategoriaTransportista categoria;
	private SedeDTO sede;
	
}
