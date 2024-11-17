package com.ipn.mx.model.entity;

import java.io.Serializable;

import com.ipn.mx.model.dto.RecursoDTO;
import com.ipn.mx.model.enumerated.EstatusRecurso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @ManyToOne
    @JoinColumn(name = "ofertaId", referencedColumnName = "idOferta", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idOfertaRS"))
    private Oferta oferta;

    @ManyToOne
    @JoinColumn(name = "vehiculoPlaca", referencedColumnName = "placa", nullable = false,
                foreignKey = @ForeignKey(name = "fk_placaRS"))
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "transportistaId", referencedColumnName = "usuarioId", nullable = false,
                foreignKey = @ForeignKey(name = "fk_usuarioIdRS"))
    private Transportista transportista;

    @ManyToOne
    @JoinColumn(name = "semirremolqueId", referencedColumnName = "idSemirremolque", nullable = true,
                foreignKey = @ForeignKey(name = "fk_idSemirremolqueRS"))
    private Semirremolque semirremolque;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = true)
    private EstatusRecurso estatus;

    
    public static Recurso toRecurso(RecursoDTO dto) {
    	Recurso r = Recurso
    			.builder()
    			.idRecurso(dto.getIdRecurso())
    			.vehiculo(Vehiculo.toVehiculo(dto.getVehiculo()))
    			.transportista(Transportista.toTransportista(dto.getTransportista()))
    			.semirremolque(Semirremolque.toSemirremolque(dto.getSemirremolque()))
    			.estatus(dto.getEstatus())
    			.build();
    	return r;
    }
}
