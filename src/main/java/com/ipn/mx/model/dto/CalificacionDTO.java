package com.ipn.mx.model.dto;

import java.math.BigDecimal;

import com.ipn.mx.model.entity.Calificacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalificacionDTO {

	private Integer idCalificacion;
    private Integer puntualidad;
    private Integer estadoCarga;
    private Integer precio;
    private Integer atencion;
    private String comentario;
    private BigDecimal promedio;
    
    public static CalificacionDTO toCalificacionDTO(Calificacion calificacion) {
    	return CalificacionDTO.builder()
    			.idCalificacion(calificacion.getIdCalificacion())
    			.puntualidad(calificacion.getPuntualidad())
    			.estadoCarga(calificacion.getEstadoCarga())
    			.precio(calificacion.getPrecio())
    			.atencion(calificacion.getAtencion())
    			.comentario(calificacion.getComentario())
    			.promedio(calificacion.getPromedio())
    			.build();
    }
}
