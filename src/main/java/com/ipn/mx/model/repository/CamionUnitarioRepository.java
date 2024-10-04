package com.ipn.mx.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipn.mx.model.entity.CamionUnitario;
import com.ipn.mx.model.entity.Vehiculo;

public interface CamionUnitarioRepository extends JpaRepository<CamionUnitario, Vehiculo> {

}
