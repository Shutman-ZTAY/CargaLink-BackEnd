package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Empresa;
import com.ipn.mx.model.entity.RepresentanteCliente;

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
	
	public static RepresentanteClienteSeguro reprClienteToRepresentanteClienteSeguro(RepresentanteCliente representanteCliente) {
		return RepresentanteClienteSeguro.builder()
				.idUsuario(representanteCliente.getIdUsuario())
				.correo(representanteCliente.getCorreo())
				.nombre(representanteCliente.getNombre())
				.primerApellido(representanteCliente.getPrimerApellido())
				.segundoApellido(representanteCliente.getSegundoApellido())
				.telefono(representanteCliente.getTelefono())
				.rol(representanteCliente.getRol())
				.empresaCliente(representanteCliente.getEmpresaCliente())
				.build();
	}
	
}
