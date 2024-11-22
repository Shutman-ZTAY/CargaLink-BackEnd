package com.ipn.mx.model.dto;

import java.util.List;

import com.ipn.mx.model.entity.VectorEmpresa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PreferenciasEmpresas {

	private VectorEmpresa preferencias;
	private List<VectorEmpresa> empresas;
}
