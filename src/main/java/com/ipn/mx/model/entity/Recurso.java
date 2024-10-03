package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ipn.mx.model.enumerated.EstatusVehiculo;
import com.ipn.mx.model.enumerated.TipoSemirremolque;

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
@Table(name = "Recurso")
public class Recurso implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idRecurso", nullable = false)
    private Integer idRecurso;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "ofertaId", referencedColumnName = "idOferta", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idOfertaRS"))
    private Oferta oferta;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "vehiculoPlaca", referencedColumnName = "placa", nullable = false,
                foreignKey = @ForeignKey(name = "fk_placaRS"))
    private Vehiculo vehiculo;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "transportistaId", referencedColumnName = "usuarioId", nullable = false,
                foreignKey = @ForeignKey(name = "fk_usuarioIdRS"))
    private Transportista transportista;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "semirremolqueId", referencedColumnName = "idSemirremolque", nullable = true,
                foreignKey = @ForeignKey(name = "fk_idSemirremolqueRS"))
    private Semirremolque semirremolque;

}
