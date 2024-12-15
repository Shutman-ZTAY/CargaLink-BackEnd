package com.ipn.mx.model.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipn.mx.model.dto.EmpresaTransporteDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	
	@Column(name = "documentoFiscal", length = 500, nullable = true)
	private String documentoFiscal;
	
	@OneToMany(mappedBy = "empresaTransporte", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonIgnore
	private List<Sede> sedes;
	
	public EmpresaTransporteDTO toEmpresaTransporteSeguro() {
		return new EmpresaTransporteDTO( 
				this.getRazonSocial(),
				this.getDescripcion(),
				this.getNombreComercial(),
				this.getRfc(),
				this.getDireccion(),
				this.getLogo());
	}
}
