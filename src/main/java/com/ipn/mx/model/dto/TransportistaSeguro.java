package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Sede;
import com.ipn.mx.model.enumerated.CategoriaTransportista;
import com.ipn.mx.model.enumerated.EstatusTransportista;
import com.ipn.mx.model.enumerated.RolUsuario;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;


@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class TransportistaSeguro extends UsuarioSeguro {

	private Integer experiencia;
    private CategoriaTransportista categoria;
    private EstatusTransportista estatusTransportista;
    private Sede sede;
    
    public TransportistaSeguro(String idUsuario, String nombre, String primerApellido, String segundoApellido,
            String correo, String telefono, RolUsuario rol, Integer experiencia, 
            CategoriaTransportista categoria, EstatusTransportista estatusTransportista, Sede sede) {
		super(idUsuario, nombre, primerApellido, segundoApellido, correo, telefono, rol);
		this.experiencia = experiencia;
		this.categoria = categoria;
		this.estatusTransportista = estatusTransportista;
		this.sede = sede;
	}
    
    
    public static TransportistaSeguro usuarioSeguroToTransportistaSeguro(UsuarioSeguro us) {
		return TransportistaSeguro.builder()
				.idUsuario(us.getIdUsuario())
				.correo(us.getCorreo())
				.nombre(us.getNombre())
				.primerApellido(us.getPrimerApellido())
				.segundoApellido(us.getSegundoApellido())
				.telefono(us.getTelefono())
				.rol(us.getRol())
				.build();
	}
    
}
