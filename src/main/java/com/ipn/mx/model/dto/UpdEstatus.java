package com.ipn.mx.model.dto;

import com.ipn.mx.model.enumerated.EstatusOferta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdEstatus {

	private EstatusOferta estatus;
	
}
