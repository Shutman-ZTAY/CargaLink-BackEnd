package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ipn.mx.model.enumerated.TipoCarga;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@JsonTypeInfo(
	    use = JsonTypeInfo.Id.NAME, 
	    include = JsonTypeInfo.As.PROPERTY, 
	    property = "tipo")
@JsonSubTypes({
	    @JsonSubTypes.Type(value = Embalaje.class, name = "EMBALAJE"),
	    @JsonSubTypes.Type(value = Suelta.class, name = "SUELTA"),
	    @JsonSubTypes.Type(value = Contenedor.class, name = "CONTENEDOR")
})
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
    
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "ofertaId", referencedColumnName = "idOferta", 
    			nullable = false, foreignKey = @ForeignKey(name = "fk_oferta_carga"))
    private Oferta oferta;
}