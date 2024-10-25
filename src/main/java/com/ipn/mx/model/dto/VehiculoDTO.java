package com.ipn.mx.model.dto;

import java.math.BigDecimal;

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
    	VehiculoDTO dto = VehiculoDTO
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
    	return dto;
    }
}
