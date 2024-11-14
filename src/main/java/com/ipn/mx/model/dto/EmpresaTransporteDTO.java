package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Empresa;
import com.ipn.mx.model.entity.EmpresaAutotransporte;
import com.ipn.mx.model.enumerated.RolUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmpresaTransporteDTO {
	private String razonSocial; 
	private String descripcion; 
	private String nombreComercial; 
	private String rfc; 
	private String direccion; 
	private String logo; 
}
