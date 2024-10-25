package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Sede;

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
    
    public static SedeDTO toSedeDTO(Sede sede) {
    	SedeDTO dto = SedeDTO
    			.builder()
    			.idSede(sede.getIdSede())
    			.nombre(sede.getNombre())
    			.direccion(sede.getDireccion())
    			.build();
    	return dto;
    }
}
