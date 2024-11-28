package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.CascadeType;
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
@Table(name = "Calificacion")
public class Calificacion implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCalificacion", nullable = false)
    private Integer idCalificacion;

    @Column(name = "puntualidad", nullable = true)
    private Integer puntualidad;

    @Column(name = "estadoCarga", nullable = true)
    private Integer estadoCarga;

    @Column(name = "precio", nullable = true)
    private Integer precio;

    @Column(name = "atencion", nullable = true)
    private Integer atencion;

    @Column(name = "comentario", length = 250, nullable = true)
    private String comentario;
    
    @Column(name = "clasificacionComentario", precision = 2, scale = 1, nullable = true)
    private BigDecimal clasificacionComentario;
    
    @Column(name = "intencidadComentario", precision = 2, scale = 1, nullable = true)
    private BigDecimal intencidadComentario;

    @Column(name = "promedio", precision = 2, scale = 1, nullable = true)
    private BigDecimal promedio;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ofertaId", referencedColumnName = "idOferta", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_ofertaIdC"))
    private Oferta oferta;
}
