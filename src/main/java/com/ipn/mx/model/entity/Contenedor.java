package com.ipn.mx.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "Contenedor")
@PrimaryKeyJoinColumn(name = "idContenedor", referencedColumnName = "idCarga")
public class Contenedor extends Carga {

	private static final long serialVersionUID = 1L;

    @Column(name = "contenido", length = 100, nullable = true)
    private String contenido;

    @Column(name = "largo", precision = 4, scale = 2, nullable = true)
    private BigDecimal largo;

    @Column(name = "ancho", precision = 4, scale = 2, nullable = true)
    private BigDecimal ancho;

    @Column(name = "alto", precision = 4, scale = 2, nullable = true)
    private BigDecimal alto;
}
