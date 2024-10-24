package com.ipn.mx.model.dto;

import java.math.BigDecimal;

import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.entity.RepresentanteTransporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostulacionDTO {

	private Integer idPostulacion;
	private Oferta oferta;
	private RepresentanteTransporteSeguro representanteTransporte;
	private BigDecimal precioPreeliminar;
	
	public PostulacionDTO(Integer idPostulacion, Oferta oferta, 
			RepresentanteTransporte representanteTransporte, BigDecimal precioPreeliminar) {
		this.idPostulacion = idPostulacion;
		this.oferta = oferta;
		this.representanteTransporte = RepresentanteTransporteSeguro.usuarioToUsuarioSeguro(representanteTransporte);
		this.precioPreeliminar = precioPreeliminar;
	}
	
}
