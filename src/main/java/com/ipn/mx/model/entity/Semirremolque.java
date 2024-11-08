package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipn.mx.model.dto.SemirremolqueDTO;
import com.ipn.mx.model.enumerated.EstatusVehiculo;
import com.ipn.mx.model.enumerated.TipoSemirremolque;

import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
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
@Table(name = "Semirremolque")
public class Semirremolque implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSemirremolque", nullable = false)
    private Integer idSemirremolque;

    @Column(name = "nombreIdentificador", length = 20, nullable = true)
    private String nombreIdentificador;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = true)
    private EstatusVehiculo estatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = true)
    private TipoSemirremolque tipo;

    @Column(name = "largo", precision = 4, scale = 2, nullable = true)
    private BigDecimal largo;

    @Column(name = "ancho", precision = 4, scale = 2, nullable = true)
    private BigDecimal ancho;

    @Column(name = "alto", precision = 4, scale = 2, nullable = true)
    private BigDecimal alto;

    @Column(name = "peso", precision = 10, scale = 3, nullable = true)
    private BigDecimal peso;

    @Column(name = "noEjes", nullable = true)
    private Integer noEjes;

    @Column(name = "noLlantas", nullable = true)
    private Integer noLlantas;

    @ManyToOne
    @JoinColumn(name = "sedeId", referencedColumnName = "idSede", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idSedeSR"))
    private Sede sede;
    
    
    @JsonIgnore
    @OneToMany(mappedBy = "semirremolque", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Recurso> recursos;


	public static Semirremolque toSemirremolque(SemirremolqueDTO dto) {
		try {
			Semirremolque s = Semirremolque
					.builder()
					.idSemirremolque(dto.getIdSemirremolque())
	    			.nombreIdentificador(dto.getNombreIdentificador())
	    			.estatus(dto.getEstatus())
	    			.tipo(dto.getTipo())
	    			.largo(dto.getLargo())
	    			.ancho(dto.getAncho())
	    			.alto(dto.getAlto())
	    			.peso(dto.getPeso())
	    			.noEjes(dto.getNoEjes())
	    			.noLlantas(dto.getNoLlantas())
	    			.sede(Sede.toSede(dto.getSede()))
					.build();
			return s;
		} catch (Exception e) {
			return null;
		}
	}
}
