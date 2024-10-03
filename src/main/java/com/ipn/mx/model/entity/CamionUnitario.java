package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ipn.mx.model.enumerated.EstatusVehiculo;
import com.ipn.mx.model.enumerated.MarcaVehiculo;
import com.ipn.mx.model.enumerated.TipoCamion;
import com.ipn.mx.model.enumerated.TipoVehiculo;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "CamionUnitario")
public class CamionUnitario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "placaCamion", referencedColumnName = "placa", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_placa"))
    private Vehiculo vehiculo;

    @Column(name = "largo", precision = 4, scale = 2, nullable = true)
    private BigDecimal largo;

    @Column(name = "ancho", precision = 4, scale = 2, nullable = true)
    private BigDecimal ancho;

    @Column(name = "alto", precision = 4, scale = 2, nullable = true)
    private BigDecimal alto;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = true)
    private TipoCamion tipo;

}
