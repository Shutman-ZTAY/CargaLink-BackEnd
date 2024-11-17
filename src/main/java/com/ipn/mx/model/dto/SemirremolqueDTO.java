package com.ipn.mx.model.dto;

import java.math.BigDecimal;

import com.ipn.mx.model.entity.Semirremolque;
import com.ipn.mx.model.enumerated.EstatusVehiculo;
import com.ipn.mx.model.enumerated.TipoSemirremolque;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SemirremolqueDTO {
	
    private Integer idSemirremolque;
    private String nombreIdentificador;
    private EstatusVehiculo estatus;
    private TipoSemirremolque tipo;
    private BigDecimal largo;
    private BigDecimal ancho;
    private BigDecimal alto;
    private BigDecimal peso;
    private Integer noEjes;
    private Integer noLlantas;
    private SedeDTO sede;
    
    public static SemirremolqueDTO toSemirremolqueDTO(Semirremolque semirremolque) {
    	SemirremolqueDTO dto = SemirremolqueDTO
    			.builder()
    			.idSemirremolque(semirremolque.getIdSemirremolque())
    			.nombreIdentificador(semirremolque.getNombreIdentificador())
    			.estatus(semirremolque.getEstatus())
    			.tipo(semirremolque.getTipo())
    			.largo(semirremolque.getLargo())
    			.ancho(semirremolque.getAncho())
    			.alto(semirremolque.getAlto())
    			.peso(semirremolque.getPeso())
    			.noEjes(semirremolque.getNoEjes())
    			.noLlantas(semirremolque.getNoLlantas())
    			.sede(SedeDTO.toSedeDTO(semirremolque.getSede()))
    			.build();
    	return dto;
    }
}
