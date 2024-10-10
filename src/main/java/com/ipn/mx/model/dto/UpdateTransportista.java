package com.ipn.mx.model.dto;

import com.ipn.mx.model.enumerated.EstatusTransportista;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateTransportista {
	
	private String password;
	private String telefono;
	private EstatusTransportista estatusTransportista;

}
