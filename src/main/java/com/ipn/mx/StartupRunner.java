package com.ipn.mx;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.ipn.mx.model.entity.Empresa;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.Transportista;
import com.ipn.mx.model.entity.Usuario;
import com.ipn.mx.model.enumerated.RolUsuario;
import com.ipn.mx.model.repository.EmpresaRepository;
import com.ipn.mx.model.repository.UsuarioRepository;

@Component
public class StartupRunner {
	/* PRUEBAS CON LA CONSOLA*/
	/*
	
	private final UsuarioRepository ur;
	private final EmpresaRepository er;
	
	public StartupRunner(UsuarioRepository ur, EmpresaRepository er) {
		this.ur = ur;
		this.er = er;
	}
	
	@Bean
    public CommandLineRunner run() {
		return args ->{
			Empresa e = Empresa.builder()
					.razonSocial("CargaLink S.A. de C.V.")
					.descripcion("Descripcion random de la empresa")
					.nombreComercial("CargaLink CDMX")
					.rfc("ABCDEFEGAJK")
					.direccion("ESCOM IPN")
					.logo("logo_cargalink.png")
					.build();
			er.save(e);
			RepresentanteCliente rc = RepresentanteCliente.builder()
					.idUsuario("USR016")
					.nombre("Ismael")
					.primerApellido("Estanislao")
					.segundoApellido("Castro")
					.correo("correo@example.com")
					.password("123456789")
					.telefono("5534398289")
					.rol(RolUsuario.REPRESENTANTE_CLIENTE)
					.empresaCliente(Empresa.builder()
							.razonSocial("CargaLink S.A. de C.V.")
							.build())
					.build();
			ur.save(rc);
		
			System.out.println("Lista de Usuarios:");
			ur.findAll().forEach(System.out::println);
		};
	}*/

}
