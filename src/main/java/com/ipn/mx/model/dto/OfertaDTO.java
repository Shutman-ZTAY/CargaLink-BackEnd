package com.ipn.mx.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ipn.mx.model.entity.Carga;
import com.ipn.mx.model.entity.Oferta;
import com.ipn.mx.model.enumerated.EstatusOferta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfertaDTO {

    private Integer idOferta;
    private String descripcion;
    private String lugarInicio;
    private LocalTime horaInicio;
    private String lugarDestino;
    private LocalTime horaTermino;
    private BigDecimal precio;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String contrato;
    private EstatusOferta estatus;
    private BigDecimal pesoTotal;
    private List<Carga> cargas;
    private RepresentanteClienteSeguro representanteCliente;
   
    public static OfertaDTO ofertatoOfertaDTO(Oferta oferta) {
    	OfertaDTO dto = OfertaDTO
    			.builder()
    			.idOferta(oferta.getIdOferta())
    			.descripcion(oferta.getDescripcion())
    			.lugarInicio(oferta.getLugarInicio())
    			.horaInicio(oferta.getHoraInicio())
    			.lugarDestino(oferta.getLugarDestino())
    			.horaTermino(oferta.getHoraTermino())
    			.precio(oferta.getPrecio())
    			.fechaInicio(oferta.getFechaInicio())
    			.fechaFin(oferta.getFechaFin())
    			.contrato(oferta.getContrato())
    			.estatus(oferta.getEstatus())
    			.pesoTotal(oferta.getPesoTotal())
    			.cargas(oferta.getCargas())
    			.representanteCliente(RepresentanteClienteSeguro.
    					reprClienteToRepresentanteClienteSeguro(oferta.getRepresentanteCliente()))
    			.build(); 
    	
    	return dto;
    }
}
