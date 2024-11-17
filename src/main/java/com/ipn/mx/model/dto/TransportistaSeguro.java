package com.ipn.mx.model.dto;

import com.ipn.mx.model.entity.Sede;
import com.ipn.mx.model.entity.Transportista;
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
    private SedeDTO sede;
    
    public TransportistaSeguro(String idUsuario, String nombre, String primerApellido, String segundoApellido,
            String correo, String telefono, RolUsuario rol, Integer experiencia, 
            CategoriaTransportista categoria, EstatusTransportista estatusTransportista, Sede sede) {
		super(idUsuario, nombre, primerApellido, segundoApellido, correo, telefono, rol);
		this.experiencia = experiencia;
		this.categoria = categoria;
		this.estatusTransportista = estatusTransportista;
		this.sede = SedeDTO.toSedeDTO(sede);
	}
    
    
    public static TransportistaSeguro toTransportistaSeguro(Transportista transportista) {
		return TransportistaSeguro.builder()
				.idUsuario(transportista.getIdUsuario())
				.correo(transportista.getCorreo())
				.nombre(transportista.getNombre())
				.primerApellido(transportista.getPrimerApellido())
				.segundoApellido(transportista.getSegundoApellido())
				.telefono(transportista.getTelefono())
				.rol(transportista.getRol())
				.experiencia(transportista.getExperiencia())
				.categoria(transportista.getCategoria())
				.estatusTransportista(transportista.getEstatusTransportista())
				.sede(SedeDTO.toSedeDTO(transportista.getSede()))
				.build();
	}
    
}
