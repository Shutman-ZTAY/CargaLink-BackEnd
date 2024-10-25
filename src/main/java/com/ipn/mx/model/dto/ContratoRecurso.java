package com.ipn.mx.model.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContratoRecurso {

	private String contrato;
	private List<RecursoDTO> recursos;
	
}
