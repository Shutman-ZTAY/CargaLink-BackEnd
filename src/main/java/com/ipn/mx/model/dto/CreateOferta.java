package com.ipn.mx.model.dto;

import java.math.BigDecimal;
import java.util.List;

import com.ipn.mx.model.entity.Carga;
import com.ipn.mx.model.entity.Oferta;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOferta {
	
    private String descripcion;
    private String lugarInicio;
    private String lugarDestino;
    private BigDecimal precio;
    private BigDecimal pesoTotal;
    private List<Carga> cargas;

    public static Oferta toOferta(CreateOferta createOferta) {
    	Oferta o = Oferta
    			.builder()
    			.descripcion(createOferta.getDescripcion())
    			.lugarInicio(createOferta.getLugarInicio())
    			.lugarDestino(createOferta.getLugarDestino())
    			.precio(createOferta.getPrecio())
    			.pesoTotal(createOferta.getPesoTotal())
    			.cargas(createOferta.getCargas())
    			.build();
    	return o;
    }
}
