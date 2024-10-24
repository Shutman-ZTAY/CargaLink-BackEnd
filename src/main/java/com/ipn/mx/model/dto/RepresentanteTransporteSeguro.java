package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.EmpresaAutotransporte;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.enumerated.EstatusRepTrans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class RepresentanteTransporteSeguro extends UsuarioSeguro {

	private EstatusRepTrans estatusRepTrans;
	private EmpresaAutotransporte empresaTransporte;
	
	public static RepresentanteTransporteSeguro usuarioToUsuarioSeguro(RepresentanteTransporte representeante) {
		return RepresentanteTransporteSeguro.builder()
				.idUsuario(representeante.getIdUsuario())
				.correo(representeante.getCorreo())
				.nombre(representeante.getNombre())
				.primerApellido(representeante.getPrimerApellido())
				.segundoApellido(representeante.getSegundoApellido())
				.telefono(representeante.getTelefono())
				.rol(representeante.getRol())
				.estatusRepTrans(representeante.getEstatusRepTrans())
				.empresaTransporte(representeante.getEmpresaTransporte())
				.build();
	}
}
