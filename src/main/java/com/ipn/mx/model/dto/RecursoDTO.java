package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Recurso;
import com.ipn.mx.model.enumerated.EstatusRecurso;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecursoDTO {

	private Integer idRecurso;
    private VehiculoDTO vehiculo;
    private TransportistaSeguro transportista;
    private SemirremolqueDTO semirremolque;
    private EstatusRecurso estatus;

    public static RecursoDTO toRecursoDTO(Recurso recurso) {
    	SemirremolqueDTO sdto = null;
    	if(recurso.getSemirremolque() != null)
    		sdto = SemirremolqueDTO.toSemirremolqueDTO(recurso.getSemirremolque());
    	
    	RecursoDTO dto = RecursoDTO
    			.builder()
    			.idRecurso(recurso.getIdRecurso())
    			.vehiculo(VehiculoDTO.toVehiculoDTO(recurso.getVehiculo()))
    			.transportista(TransportistaSeguro.toTransportistaSeguro(recurso.getTransportista()))
    			.semirremolque(sdto)
    			.estatus(recurso.getEstatus())
    			.build();
    	return dto;
    }
}
