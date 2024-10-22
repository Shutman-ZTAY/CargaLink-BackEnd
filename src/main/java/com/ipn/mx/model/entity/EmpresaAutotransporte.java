package com.ipn.mx.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
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
@Table(name = "EmpresaAutotransporte")
@PrimaryKeyJoinColumn(name = "transporteRazon", referencedColumnName = "razonSocial")
public class EmpresaAutotransporte extends Empresa{

	private static final long serialVersionUID = 1L;
	
	@Column(name = "documentoFiscal", length = 80, nullable = true)
	private String documentoFiscal;
	
	@OneToMany(mappedBy = "empresaTransporte", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Sede> sedes;

}
