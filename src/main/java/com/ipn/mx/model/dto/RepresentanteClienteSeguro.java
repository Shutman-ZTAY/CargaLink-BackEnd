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
	
	public static RepresentanteClienteSeguro usuarioSeguroToRepresentanteClienteSeguro(UsuarioSeguro us) {
		return RepresentanteClienteSeguro.builder()
				.idUsuario(us.getIdUsuario())
				.correo(us.getCorreo())
				.nombre(us.getNombre())
				.primerApellido(us.getPrimerApellido())
				.segundoApellido(us.getSegundoApellido())
				.telefono(us.getTelefono())
				.rol(us.getRol())
				.build();
	}
	
}
