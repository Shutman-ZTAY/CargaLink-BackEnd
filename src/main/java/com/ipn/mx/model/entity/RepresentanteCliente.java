package com.ipn.mx.model.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Table(name = "RepresentanteCliente")
@PrimaryKeyJoinColumn(name = "usuarioId", referencedColumnName = "idUsuario")
public class RepresentanteCliente extends Usuario {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "empresaCliente", referencedColumnName = "razonSocial", 
    			foreignKey = @ForeignKey(name = "fk_clienteRazonRC"))
	private Empresa empresaCliente;
}
