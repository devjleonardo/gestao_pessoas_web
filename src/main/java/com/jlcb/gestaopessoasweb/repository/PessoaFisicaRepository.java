package com.jlcb.gestaopessoasweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jlcb.gestaopessoasweb.model.PessoaFisica;

@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {
	
    @Query("SELECT pf.id FROM PessoaFisica pf ORDER BY pf.id ASC")
    Long encontrarPrimeiroId();

	@Query("SELECT pf FROM PessoaFisica pf LEFT JOIN FETCH pf.enderecos WHERE pf.id = :id")
    Optional<PessoaFisica> buscarComEnderecosPorId(@Param("id") Long id);
	
    @Query("SELECT pf FROM PessoaFisica pf LEFT JOIN FETCH pf.enderecos")
    List<PessoaFisica> findAll();
	

}
