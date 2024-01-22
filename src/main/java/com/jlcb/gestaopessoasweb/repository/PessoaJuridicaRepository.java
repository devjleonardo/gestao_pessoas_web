package com.jlcb.gestaopessoasweb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jlcb.gestaopessoasweb.model.PessoaJuridica;

@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long> {
	
    @Query("SELECT pj.id FROM PessoaJuridica pj ORDER BY pj.id ASC")
    Long encontrarPrimeiroId();

	@Query("SELECT pj FROM PessoaJuridica pj LEFT JOIN FETCH pj.enderecos WHERE pj.id = :id")
    Optional<PessoaJuridica> buscarComEnderecosPorId(@Param("id") Long id);
	
    @Query("SELECT pj FROM PessoaJuridica pj LEFT JOIN FETCH pj.enderecos")
    List<PessoaJuridica> findAll();
	

}
