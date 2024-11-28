package com.ipn.mx.model.dto;

import com.ipn.mx.model.enumerated.EstatusOferta;
import com.ipn.mx.model.enumerated.EstatusRecurso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdEstatus {

	private EstatusRecurso estatus;
	
}
