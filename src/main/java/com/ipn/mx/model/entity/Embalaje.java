package com.ipn.mx.model.entity;

import java.math.BigDecimal;

import com.ipn.mx.model.enumerated.TipoEmbalaje;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Embalaje")
@PrimaryKeyJoinColumn(name = "idEmbalaje", referencedColumnName = "idCarga")
public class Embalaje extends Carga {

	private static final long serialVersionUID = 1L;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoEmbalaje", nullable = false)
    private TipoEmbalaje tipoEmbalaje;

    @Column(name = "contenido", length = 100, nullable = true)
    private String contenido;

    @Column(name = "noUnidades", nullable = true)
    private Integer noUnidades;

    @Column(name = "pesoUnitario", precision = 8, scale = 3, nullable = true)
    private BigDecimal pesoUnitario;

    @Column(name = "largo", precision = 4, scale = 2, nullable = true)
    private BigDecimal largo;

    @Column(name = "ancho", precision = 4, scale = 2, nullable = true)
    private BigDecimal ancho;

    @Column(name = "alto", precision = 4, scale = 2, nullable = true)
    private BigDecimal alto; 
}
