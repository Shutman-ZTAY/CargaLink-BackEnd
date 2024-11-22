package com.ipn.mx.service.implementation;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ipn.mx.model.dto.PreferenciasEmpresas;
import com.ipn.mx.model.entity.Calificacion;
import com.ipn.mx.model.entity.Postulacion;
import com.ipn.mx.model.entity.RepresentanteCliente;
import com.ipn.mx.model.entity.RepresentanteTransporte;
import com.ipn.mx.model.entity.VectorEmpresa;
import com.ipn.mx.model.enumerated.TipoEmpresa;
import com.ipn.mx.model.repository.CalificacionRepository;
import com.ipn.mx.model.repository.VectorEmpresaRepository;
import com.ipn.mx.service.interfaces.VectorEmpresaService;

@Service
public class VectorEmpresaServiceImpl implements VectorEmpresaService {
	
	@Autowired
	private VectorEmpresaRepository vectorEmpresaRepository;
	@Autowired
	private CalificacionRepository calificacionRepository;

	@Override
	public void changeAverge(Calificacion calificacion) {
		RepresentanteTransporte rt = calificacion.getOferta().getPostulaciones().get(0).getRepresentanteTransporte();
		RepresentanteCliente rc = calificacion.getOferta().getRepresentanteCliente();
		//Si el comentario se clasifica como positivo, se añade a las preferencias del representante cliente
		if (calificacion.getClasificacionComentario().compareTo(BigDecimal.valueOf(0.2499999999)) > 0) {
			VectorEmpresa v = vectorEmpresaRepository.findByIdEmpresa(rc.getEmpresaCliente().getRazonSocial()).orElse(null); 
			if(v == null) {
				v = VectorEmpresa.buildNewVectorEmpresa(calificacion, rc.getEmpresaCliente(), TipoEmpresa.CLIENTE);
				vectorEmpresaRepository.save(v);
			} else {
				VectorEmpresa newVec = VectorEmpresa.buildNewVectorEmpresa(calificacion, null, null);
				v = VectorEmpresa.promedioVectorEmpresa(v, newVec, calificacionRepository.countByRepresentanteCliente(rc.getIdUsuario()));
				vectorEmpresaRepository.save(v);
			}
		}
		VectorEmpresa v = vectorEmpresaRepository.findByIdEmpresa(rt.getEmpresaTransporte().getRazonSocial()).orElse(null);
		if (v == null) {
			v = VectorEmpresa.buildNewVectorEmpresa(calificacion, rt.getEmpresaTransporte(), TipoEmpresa.TRANSPORTE);
			vectorEmpresaRepository.save(v);
		} else {
			VectorEmpresa newVec = VectorEmpresa.buildNewVectorEmpresa(calificacion, null, null);
			v = VectorEmpresa.promedioVectorEmpresa(v, newVec, calificacionRepository.countByRepresentanteTransporte(rt.getIdUsuario()));
			vectorEmpresaRepository.save(v);
		}
	}

	/**
	 * Retorna null si el representante cliente aun no tiene establecido un vector de preferencias
	 * */
	@Override
	public PreferenciasEmpresas getPreferenciasEmpresa(String idRepresentanteCliente, List<Postulacion> postulaciones) {
		VectorEmpresa preferencias = vectorEmpresaRepository.findByRepresentanteCliente(idRepresentanteCliente).orElse(null);
		if (preferencias == null)
			return null;
		List<Integer> idPostulaciones = new ArrayList<Integer>();
		for (Postulacion postulacion : postulaciones) {
			idPostulaciones.add(postulacion.getIdPostulacion());
		}
		List<VectorEmpresa> vecEmpresas =  vectorEmpresaRepository.findByPostulaciones(idPostulaciones);
		return PreferenciasEmpresas.builder()
				.preferencias(preferencias)
				.empresas(vecEmpresas)
				.build();
	}

}
