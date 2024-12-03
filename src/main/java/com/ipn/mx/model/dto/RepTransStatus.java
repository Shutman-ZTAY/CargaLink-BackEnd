package com.ipn.mx.model.dto;

import com.google.auto.value.AutoValue.Builder;
import com.ipn.mx.model.enumerated.EstatusRepTrans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RepTransStatus {
	
	private EstatusRepTrans estatus;

}
