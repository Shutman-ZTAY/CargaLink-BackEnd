package com.ipn.mx.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ipn.mx.model.entity.Empresa;

public interface EmpresaRepository extends JpaRepository<Empresa, String> {

}
