package com.ipn.mx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalificacionToken {

    private Integer puntualidad;
    private Integer estadoCarga;
    private Integer precio;
    private Integer atencion;
    private String comentario;
	private String token;
	
}
