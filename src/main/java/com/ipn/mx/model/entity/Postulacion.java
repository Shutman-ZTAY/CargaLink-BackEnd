package com.ipn.mx.model.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Postulacion")
public class Postulacion {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPostulacion", nullable = false)
	private Integer idPostulacion;
	
	@ManyToOne
    @JoinColumn(name = "ofertaId", referencedColumnName = "idOferta", nullable = false,
                foreignKey = @ForeignKey(name = "fk_oferta_postulacion"))
	private Oferta oferta;
	
	@ManyToOne
    @JoinColumn(name = "representanteTransporteId", referencedColumnName = "usuarioId", nullable = false,
                foreignKey = @ForeignKey(name = "fk_reprTransporte_postulacion"))
	private RepresentanteTransporte representanteTransporte;
	
	@Column(name = "precioPreeliminar", precision = 10, scale = 2, nullable = false)
	private BigDecimal precioPreeliminar;
	
}
