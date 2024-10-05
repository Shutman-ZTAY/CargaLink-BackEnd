package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.EmpresaAutotransporte;
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
	
}
