package com.ipn.mx.model.entity;

import com.ipn.mx.model.enumerated.TipoCamion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "CamionUnitario")
@PrimaryKeyJoinColumn(name = "placaCamion", referencedColumnName = "placa")
public class CamionUnitario extends Vehiculo{
	
	private static final long serialVersionUID = 1L;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoCamion", nullable = false)
    private TipoCamion tipoCamion;

}
