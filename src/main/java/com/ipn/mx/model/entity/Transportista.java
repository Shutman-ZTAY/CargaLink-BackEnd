package com.ipn.mx.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipn.mx.model.dto.TransportistaSeguro;
import com.ipn.mx.model.enumerated.CategoriaTransportista;
import com.ipn.mx.model.enumerated.EstatusTransportista;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Entity
@Table(name = "Transportista")
@PrimaryKeyJoinColumn(name = "usuarioId", referencedColumnName = "idUsuario")
public class Transportista extends Usuario {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "experiencia", nullable = false)
    private Integer experiencia;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria", nullable = false)
    private CategoriaTransportista categoria;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatusTransportista")
    private EstatusTransportista estatusTransportista;

    @ManyToOne
    @JoinColumn(name = "sede", referencedColumnName = "idSede", 
    			foreignKey = @ForeignKey(name = "idSedeT"), nullable = false)
    private Sede sede;
    
    @JsonIgnore
    @OneToMany(mappedBy = "transportista", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Recurso> recursos;

	public static Transportista toTransportista(TransportistaSeguro transportistaSeguro) {
		try {
			Transportista t = Transportista
					.builder()
					.idUsuario(transportistaSeguro.getIdUsuario())
					.correo(transportistaSeguro.getCorreo())
					.nombre(transportistaSeguro.getNombre())
					.primerApellido(transportistaSeguro.getPrimerApellido())
					.segundoApellido(transportistaSeguro.getSegundoApellido())
					.telefono(transportistaSeguro.getTelefono())
					.rol(transportistaSeguro.getRol())
					.experiencia(transportistaSeguro.getExperiencia())
					.categoria(transportistaSeguro.getCategoria())
					.estatusTransportista(transportistaSeguro.getEstatusTransportista())
					.sede(Sede.toSede(transportistaSeguro.getSede()))
					.build();
			return t;
		} catch (Exception e) {
			return null;
		}
	}
}
