package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.CamionUnitario;
import com.ipn.mx.model.enumerated.TipoCamion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class CamionUnitarioDTO extends VehiculoDTO {

	private TipoCamion tipoCamion;
	
	public static CamionUnitarioDTO toCamionUnitarioDTO(CamionUnitario camionUnitario) {
		VehiculoDTO vdto = VehiculoDTO.toVehiculoDTO(camionUnitario);
		CamionUnitarioDTO dto = (CamionUnitarioDTO) vdto;
		dto.setTipoCamion(camionUnitario.getTipoCamion());
		return dto;
	}
}
