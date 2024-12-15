package com.ipn.mx.model.dto;

import com.google.auto.value.AutoValue.Builder;
import com.ipn.mx.model.enumerated.TipoCamion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPassword {

	private String token;
	private String password;
	
}
