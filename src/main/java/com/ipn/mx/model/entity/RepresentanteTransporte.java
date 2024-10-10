package com.ipn.mx.model.entity;

import com.ipn.mx.model.enumerated.EstatusRepTrans;

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
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@Entity
@Table(name = "RepresentanteTransporte")
@PrimaryKeyJoinColumn(name = "usuarioId", referencedColumnName = "idUsuario")
public class RepresentanteTransporte extends Usuario {

	private static final long serialVersionUID = 1L;

	@Enumerated(EnumType.STRING)
    @Column(name = "estatusRepTrans")
	private EstatusRepTrans estatusRepTrans;
	
	@ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "empresaTransporte", referencedColumnName = "transporteRazon", 
    			foreignKey = @ForeignKey(name = "fk_transporteRazonRT"))
	private EmpresaAutotransporte empresaTransporte;
}
