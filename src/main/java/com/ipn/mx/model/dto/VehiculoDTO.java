package com.ipn.mx.model.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ipn.mx.model.entity.CamionUnitario;
import com.ipn.mx.model.entity.Vehiculo;
import com.ipn.mx.model.enumerated.EstatusVehiculo;
import com.ipn.mx.model.enumerated.MarcaVehiculo;
import com.ipn.mx.model.enumerated.TipoVehiculo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME, 
	    include = JsonTypeInfo.As.PROPERTY, 
	    property = "tipo")
@JsonSubTypes({
	    @JsonSubTypes.Type(value = VehiculoDTO.class, name = "TRACTOCAMION"),
	    @JsonSubTypes.Type(value = CamionUnitarioDTO.class, name = "CAMION_UNITARIO")
})
public class VehiculoDTO {
	
    private String placa;
    private BigDecimal peso;
    private Integer noEjes;
    private Integer noLlantas;
    private BigDecimal largo;
    private MarcaVehiculo marca;
    private TipoVehiculo tipo;
    private EstatusVehiculo estatus;
    private String modelo;
    private SedeDTO sede;
    
    public static VehiculoDTO toVehiculoDTO(Vehiculo vehiculo) {
    	VehiculoDTO dto;
    	if (vehiculo instanceof CamionUnitario) {
    		dto = CamionUnitarioDTO
        			.builder()
        			.placa(vehiculo.getPlaca())
        			.peso(vehiculo.getPeso())
        			.noEjes(vehiculo.getNoEjes())
        			.noLlantas(vehiculo.getNoLlantas())
        			.largo(vehiculo.getLargo())
        			.marca(vehiculo.getMarca())
        			.tipo(vehiculo.getTipo())
        			.estatus(vehiculo.getEstatus())
        			.modelo(vehiculo.getModelo())
        			.sede(SedeDTO.toSedeDTO(vehiculo.getSede()))
        			.tipoCamion(((CamionUnitario) vehiculo).getTipoCamion())
        			.build();;
    	} else {
    		dto = VehiculoDTO
        			.builder()
        			.placa(vehiculo.getPlaca())
        			.peso(vehiculo.getPeso())
        			.noEjes(vehiculo.getNoEjes())
        			.noLlantas(vehiculo.getNoLlantas())
        			.largo(vehiculo.getLargo())
        			.marca(vehiculo.getMarca())
        			.tipo(vehiculo.getTipo())
        			.estatus(vehiculo.getEstatus())
        			.modelo(vehiculo.getModelo())
        			.sede(SedeDTO.toSedeDTO(vehiculo.getSede()))
        			.build();
    	}
    	return dto;
    }
}
