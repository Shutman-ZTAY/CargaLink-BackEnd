package com.ipn.mx.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipn.mx.model.entity.Mensaje;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {

}
