package com.ipn.mx.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRepcli {
	private String nombre;
	private String primerApellido;
	private String segundoApellido;
	private String correo;
	private String telefono;
	private String password;
	private String newpass;
}
