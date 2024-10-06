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
    
    public static UsuarioSeguro usuarioToUsuarioSeguro(Usuario u) {
		return UsuarioSeguro.builder()
				.idUsuario(u.getIdUsuario())
				.correo(u.getCorreo())
				.nombre(u.getNombre())
				.primerApellido(u.getPrimerApellido())
				.segundoApellido(u.getSegundoApellido())
				.telefono(u.getTelefono())
				.rol(u.getRol())
				.build();
    }

}
