package com.ipn.mx.model.dto;

import java.time.LocalDateTime;

import com.ipn.mx.model.entity.Empresa;
import com.ipn.mx.model.entity.EmpresaAutotransporte;
import com.ipn.mx.model.enumerated.RolUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioSeguroMensaje {
	private String idUsuario;
    private String nombre;
    private String primerApellido;
    private String segundoApellido;
    private String correo;
    private String telefono;
    private RolUsuario rol;
    private EmpresaTransporteDTO empresaTransporte; 
    private Empresa empresaCliente;
}
