package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ipn.mx.model.enumerated.TipoCarga;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Carga")
public class Carga implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCarga", nullable = false)
    private Integer idCarga;

    @Column(name = "pesoTotal", precision = 8, scale = 3, nullable = true)
    private BigDecimal pesoTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = true)
    private TipoCarga tipo;
}