package com.ipn.mx.model.entity;

import com.ipn.mx.model.enumerated.CategoriaTransportista;
import com.ipn.mx.model.enumerated.EstatusTransportista;
import com.ipn.mx.model.enumerated.RolUsuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
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

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "sede", referencedColumnName = "idSede", 
    			foreignKey = @ForeignKey(name = "idSedeT"))
    private Sede sede;
}