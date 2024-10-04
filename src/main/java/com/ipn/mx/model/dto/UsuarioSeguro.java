package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class UsuarioSeguro {
	
    private String idUsuario;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String correo;
    private String telefono;
    private RolUsuario rol;
    
}
