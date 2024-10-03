package com.ipn.mx.model.entity;

import java.io.Serializable;

import com.ipn.mx.model.enumerated.RolUsuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
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
@Table(name = "Usuario")
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "idUsuario", nullable = false, length = 13)
    private String idUsuario;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "primerApellido", nullable = false, length = 50)
    private String primerApellido;

    @Column(name = "segundoApellido", length = 50)
    private String segundoApellido;

    @Email
    @Column(name = "correo", nullable = false, length = 255)
    private String correo;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "telefono", nullable = false, length = 10)
    private String telefono;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false)
    private RolUsuario rol;
}
