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
@Table(name = "OfertaCarga")
public class OfertaCarga implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOfertaCarga", nullable = false)
    private Integer idOfertaCarga;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ofertaId", referencedColumnName = "idOferta", nullable = true,
                foreignKey = @ForeignKey(name = "fk_idOfertaOC"))
    private Oferta oferta;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cargaId", referencedColumnName = "idCarga", nullable = true,
                foreignKey = @ForeignKey(name = "fk_idCargaOC"))
    private Carga carga;
    
}
