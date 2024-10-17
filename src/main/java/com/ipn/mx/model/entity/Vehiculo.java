package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ipn.mx.model.enumerated.EstatusVehiculo;
import com.ipn.mx.model.enumerated.MarcaVehiculo;
import com.ipn.mx.model.enumerated.TipoVehiculo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "Vehiculo")
public class Vehiculo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @Column(name = "placa", length = 6, nullable = false)
    private String placa;

    @Column(name = "peso", precision = 10, scale = 3, nullable = true)
    private BigDecimal peso;

    @Column(name = "noEjes", nullable = true)
    private Integer noEjes;

    @Column(name = "noLlantas", nullable = true)
    private Integer noLlantas;
    
    @Column(name = "largo", precision = 4, scale = 2, nullable = true)
    private BigDecimal largo;

    @Enumerated(EnumType.STRING)
    @Column(name = "marca", nullable = true)
    private MarcaVehiculo marca;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = true)
    private TipoVehiculo tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estatus", nullable = true)
    private EstatusVehiculo estatus;

    @Column(name = "modelo", length = 45, nullable = true)
    private String modelo;
    
    @ManyToOne
    @JoinColumn(name = "sedeId", referencedColumnName = "idSede", nullable = false,
                foreignKey = @ForeignKey(name = "fk_idSedeVh"))
    private Sede sede;

}
