package com.ipn.mx.model.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "Empresa")
public class Empresa implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "razonSocial", nullable = false, length = 100)
    private String razonSocial;

    @Column(name = "descripcion", nullable = false, length = 100)
    private String descripcion;

    @Column(name = "nombreComercial", nullable = false, length = 50)
    private String nombreComercial;

    @Column(name = "rfc", length = 13, nullable = true)
    private String rfc;

    @Column(name = "direccion", length = 100, nullable = true)
    private String direccion;

    @Column(name = "logo", length = 60, nullable = true)
    private String logo;
	
}
