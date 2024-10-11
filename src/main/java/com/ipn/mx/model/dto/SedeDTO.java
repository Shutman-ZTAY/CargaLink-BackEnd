package com.ipn.mx.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SedeDTO {
	
	private Integer idSede;
    private String nombre;
    private String direccion;
    
}
