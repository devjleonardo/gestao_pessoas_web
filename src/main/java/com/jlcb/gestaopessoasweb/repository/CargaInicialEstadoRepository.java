package com.jlcb.gestaopessoasweb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jlcb.gestaopessoasweb.model.CargaInicialEstado;

@Repository
public interface CargaInicialEstadoRepository extends JpaRepository<CargaInicialEstado, Long> {

}
