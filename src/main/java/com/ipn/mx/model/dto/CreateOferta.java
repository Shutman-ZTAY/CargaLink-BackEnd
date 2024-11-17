package com.ipn.mx.model.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.ipn.mx.model.entity.Carga;

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
    private LocalDate fechaFin;
    private LocalTime horaTermino;
    private List<Carga> cargas;

}
