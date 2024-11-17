package com.ipn.mx.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
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
@Entity
@Table(name = "RepresentanteCliente")
@PrimaryKeyJoinColumn(name = "usuarioId", referencedColumnName = "idUsuario")
public class RepresentanteCliente extends Usuario {

	private static final long serialVersionUID = 1L;

	@ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "empresaCliente", referencedColumnName = "razonSocial", 
    			foreignKey = @ForeignKey(name = "fk_clienteRazonRC"), nullable = true)
	private Empresa empresaCliente;
	
	@JsonIgnore
	@OneToMany(mappedBy = "representanteCliente", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Oferta> ofertas;
}
