package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ipn.mx.model.dto.SedeDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "Sede")
public class Sede implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSede", nullable = false)
    private Integer idSede;

    @Column(name = "nombre", length = 45, nullable = false)
    private String nombre;

    @Column(name = "direccion", length = 100, nullable = false)
    private String direccion;

    @ManyToOne//(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "empresaTransporte", referencedColumnName = "transporteRazon", nullable = false, 
                foreignKey = @ForeignKey(name = "fk_transporteRazon"))
    private EmpresaAutotransporte empresaTransporte;
    
    @OneToMany(mappedBy = "sede", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Transportista> transportistas;
    
    @OneToMany(mappedBy = "sede", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vehiculo> vehiculos;
    
    @OneToMany(mappedBy = "sede", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Semirremolque> semirremolques;

	public static Sede toSede(SedeDTO dto) {
		Sede s = Sede
				.builder()
				.idSede(dto.getIdSede())
    			.nombre(dto.getNombre())
    			.direccion(dto.getDireccion())
				.build();
		return s;
	}
}
