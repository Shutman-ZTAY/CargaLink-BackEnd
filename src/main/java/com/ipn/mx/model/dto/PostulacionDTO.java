package com.ipn.mx.model.dto;

import java.math.BigDecimal;

import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.entity.Postulacion;
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
	private OfertaDTO oferta;
	private RepresentanteTransporteSeguro representanteTransporte;
	private BigDecimal precioPreeliminar;
	
	public PostulacionDTO(Integer idPostulacion, Oferta oferta, 
			RepresentanteTransporte representanteTransporte, BigDecimal precioPreeliminar) {
		this.idPostulacion = idPostulacion;
		this.oferta = OfertaDTO.ofertatoOfertaDTO(oferta);
		this.representanteTransporte = RepresentanteTransporteSeguro.usuarioToUsuarioSeguro(representanteTransporte);
		this.precioPreeliminar = precioPreeliminar;
	}
	
	public static PostulacionDTO toPostulacionDTO(Postulacion postulacion) {
		PostulacionDTO dto = PostulacionDTO
				.builder()
				.idPostulacion(postulacion.getIdPostulacion())
				.oferta(OfertaDTO.ofertatoOfertaDTO(postulacion.getOferta()))
				.representanteTransporte(RepresentanteTransporteSeguro
						.usuarioToUsuarioSeguro(postulacion.getRepresentanteTransporte()))
				.precioPreeliminar(postulacion.getPrecioPreeliminar())
				.build();
		return dto;
	}
	
}
