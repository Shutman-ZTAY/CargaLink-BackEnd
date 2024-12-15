package com.ipn.mx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateEmpresaTransporte {
	private String nombreComercial;
	private String rfc;
	private String direccion;
	private String descripcion;
	private String password;
}
