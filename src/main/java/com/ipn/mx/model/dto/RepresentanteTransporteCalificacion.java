package com.ipn.mx.model.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.ipn.mx.model.entity.Calificacion;
import com.ipn.mx.model.entity.RepresentanteTransporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepresentanteTransporteCalificacion {

	private RepresentanteTransporteSeguro representanteTransporte;
	private List<CalificacionDTO> calificaciones;
	
	public static RepresentanteTransporteCalificacion toRepresentanteTransporteCalificacion(
			RepresentanteTransporte representanteTransporte, List<Calificacion> calificaciones) {
		List<CalificacionDTO> ldto = new ArrayList<CalificacionDTO>();
		for (Calificacion calificacion : calificaciones) {
			ldto.add(CalificacionDTO.toCalificacionDTO(calificacion));
		}
		return RepresentanteTransporteCalificacion.builder()
				.representanteTransporte(RepresentanteTransporteSeguro.usuarioToUsuarioSeguro(representanteTransporte))
				.calificaciones(ldto)
				.build();
	}
}
