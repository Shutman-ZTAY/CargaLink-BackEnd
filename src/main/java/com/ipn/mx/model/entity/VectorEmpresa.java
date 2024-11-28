package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.ipn.mx.model.enumerated.TipoEmpresa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Esta tabla contiene un vector caracteristico de una empresa
 * 
 * -Para las empresas de transporte el vector describe sus servicios
 * -Para las empresas cliente el vector describe los aspectos que prefieren
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "VectorEmpresa")
public class VectorEmpresa implements Serializable { 
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idVector", nullable = false)
	private Integer idVector;

	@OneToOne
	@JoinColumn(name = "empresaId", referencedColumnName = "razonSocial", nullable = false,
    			foreignKey = @ForeignKey(name = "fk_vector_empresa"))
	private Empresa empresa;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "tipoEmpresa", nullable = false)
	private TipoEmpresa tipoEmpresa;
	
	@Column(name = "puntualidad", precision = 10, scale = 5, nullable = false)
	private BigDecimal puntualidad;

    @Column(name = "estadoCarga", precision = 10, scale = 5, nullable = false)
    private BigDecimal estadoCarga;

    @Column(name = "precio", precision = 10, scale = 5, nullable = false)
    private BigDecimal precio;

    @Column(name = "atencion", precision = 10, scale = 5, nullable = false)
    private BigDecimal atencion;
    
    @Column(name = "clasificacionComentario", precision = 10, scale = 5, nullable = false)
    private BigDecimal clasificacionComentario;
    
    
    public VectorEmpresa(Integer idVector, String razonSocial, TipoEmpresa tipoEmpresa, BigDecimal puntualidad, 
    		BigDecimal estadoCarga, BigDecimal precio, BigDecimal atencion, BigDecimal clasificacionComentario) {
    	this.idVector = idVector;
    	this.empresa = Empresa.builder().razonSocial(razonSocial).build();
    	this.tipoEmpresa = tipoEmpresa;
    	this.puntualidad = puntualidad;
    	this.estadoCarga = estadoCarga;
    	this.precio = precio;
    	this.atencion = atencion;
    	this.clasificacionComentario = clasificacionComentario;
    }
    
    public static VectorEmpresa buildNewVectorEmpresa(Calificacion calificacion, Empresa empresa, TipoEmpresa tipoEmpresa) {
    	VectorEmpresa v = VectorEmpresa.builder()
				.empresa(empresa)
				.tipoEmpresa(tipoEmpresa)
				.puntualidad(calificacion.getIntencidadComentario().multiply(BigDecimal.valueOf(calificacion.getPuntualidad())))
				.estadoCarga(calificacion.getIntencidadComentario().multiply(BigDecimal.valueOf(calificacion.getEstadoCarga())))
				.precio(calificacion.getIntencidadComentario().multiply(BigDecimal.valueOf(calificacion.getPrecio())))
				.atencion(calificacion.getIntencidadComentario().multiply(BigDecimal.valueOf(calificacion.getAtencion())))
				.clasificacionComentario(calificacion.getIntencidadComentario().multiply(calificacion.getClasificacionComentario()))
				.build();
    	return v;	
    }
    
    public static VectorEmpresa promedioVectorEmpresa(VectorEmpresa vectorBD, VectorEmpresa vectorNuevo, Long vectoresEnBD) {
    	BigDecimal nbd = BigDecimal.valueOf(vectoresEnBD);
    	BigDecimal newNbd = nbd.add(BigDecimal.valueOf(1));
    	vectorBD.setPuntualidad((vectorBD.getPuntualidad().multiply(nbd))
    			.add(vectorNuevo.getPuntualidad())
    			.divide(newNbd, 5, RoundingMode.HALF_UP));
    	vectorBD.setEstadoCarga((vectorBD.getEstadoCarga().multiply(nbd))
    			.add(vectorNuevo.getEstadoCarga())
    			.divide(newNbd, 5, RoundingMode.HALF_UP));
    	vectorBD.setPrecio((vectorBD.getPrecio().multiply(nbd))
    			.add(vectorNuevo.getPrecio())
    			.divide(newNbd, 5, RoundingMode.HALF_UP));
    	vectorBD.setAtencion((vectorBD.getAtencion().multiply(nbd))
    			.add(vectorNuevo.getAtencion())
    			.divide(newNbd, 5, RoundingMode.HALF_UP));
    	vectorBD.setClasificacionComentario((vectorBD.getClasificacionComentario().multiply(nbd))
    			.add(vectorNuevo.getClasificacionComentario())
    			.divide(newNbd, 5, RoundingMode.HALF_UP));
    	return vectorBD;
    }
}
