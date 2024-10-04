package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.ipn.mx.model.enumerated.EstatusOferta;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "Oferta")
public class Oferta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOferta", nullable = false)
    private Integer idOferta;

    @Column(name = "descripcion", length = 250, nullable = true)
    private String descripcion;

    @Column(name = "lugarInicio", length = 150, nullable = true)
    private String lugarInicio;

    @Column(name = "horaInicio", nullable = true)
    private LocalTime horaInicio;

    @Column(name = "lugarDestino", length = 150, nullable = true)
    private String lugarDestino;

    @Column(name = "horaTermino", nullable = true)
    private LocalTime horaTermino;

    @Column(name = "precio", precision = 10, scale = 2, nullable = true)
    private BigDecimal precio;

    @Column(name = "fechaInicio", nullable = true)
    private LocalDate fechaInicio;

    @Column(name = "fechaFin", nullable = true)
    private LocalDate fechaFin;

    @Column(name = "contrato", length = 150, nullable = true)
    private String contrato;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = true)
    private EstatusOferta estatus;

    @Column(name = "pesoTotal", precision = 8, scale = 3, nullable = true)
    private BigDecimal pesoTotal;
}