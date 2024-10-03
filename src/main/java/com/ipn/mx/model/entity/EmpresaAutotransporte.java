package com.ipn.mx.model.entity;

import java.io.Serializable;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
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
@Table(name = "EmpresaAutotransporte")
public class EmpresaAutotransporte implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@ManyToOne(cascade = CascadeType.REMOVE)
	@JoinColumn(name = "transporteRazon", referencedColumnName = "razonSocial", nullable = false, 
				foreignKey = @ForeignKey(name = "fk_razonSocial"))
	private Empresa transporteRazon;
	@Column(name = "documentoFiscal", length = 80, nullable = true)
	private String documentoFiscal;

}
