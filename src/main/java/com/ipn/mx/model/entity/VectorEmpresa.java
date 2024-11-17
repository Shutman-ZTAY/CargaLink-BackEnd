package com.ipn.mx.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.ipn.mx.model.enumerated.EstatusRecurso;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
	
	@Column(name = "puntualidad", nullable = false)
	private Integer puntualidad;

    @Column(name = "estadoCarga", nullable = false)
    private Integer estadoCarga;

    @Column(name = "precio", nullable = false)
    private Integer precio;

    @Column(name = "atencion", nullable = false)
    private Integer atencion;

    @Column(name = "comentario", length = 250, nullable = false)
    private String comentario;
    
    @Column(name = "clasificacionComentario", precision = 10, scale = 5, nullable = false)
    private BigDecimal clasificacionComentario;
    
    @Column(name = "intencidadComentario", precision = 10, scale = 5, nullable = false)
    private BigDecimal intencidadComentario;

    @Column(name = "promedio", precision = 10, scale = 5, nullable = false)
    private BigDecimal promedio;
}
