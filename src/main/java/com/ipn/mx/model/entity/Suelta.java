package com.ipn.mx.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Suelta")
@PrimaryKeyJoinColumn(name = "idSuelta", referencedColumnName = "idCarga")
public class Suelta extends Carga {

	private static final long serialVersionUID = 1L;

    @Column(name = "descripcion", length = 100, nullable = true)
    private String descripcion;
}

