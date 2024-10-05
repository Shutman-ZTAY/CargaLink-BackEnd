package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Empresa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RepresentanteClienteSeguro extends UsuarioSeguro {

	private Empresa empresaCliente;
	
}
