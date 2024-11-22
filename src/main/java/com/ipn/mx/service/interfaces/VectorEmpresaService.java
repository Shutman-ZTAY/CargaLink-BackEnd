package com.ipn.mx.service.interfaces;

import java.util.List;

import com.ipn.mx.model.dto.PreferenciasEmpresas;
import com.ipn.mx.model.entity.Calificacion;
import com.ipn.mx.model.entity.Postulacion;

public interface VectorEmpresaService {

	public void changeAverge(Calificacion calificacion);

	public PreferenciasEmpresas getPreferenciasEmpresa(String idRepresentanteCliente, List<Postulacion> postulaciones);
	
}
