package com.jlcb.gestaopessoasweb.api.dto.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

public class PessoaFisicaResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(example = "1")
	private Long id;
	
	@Schema(example = "João da Silva")
	private String nome;
	
	@Schema(example = "45878745745")
	private String cpf;
	
	@Schema(example = "joao_da_silva@gmail.com")
	private String email;
	
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<EnderecoResponseDTO> enderecos;

	public PessoaFisicaResponseDTO() {
	}
	
	public PessoaFisicaResponseDTO(Long id, String nome, String cpf, String email) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<EnderecoResponseDTO> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<EnderecoResponseDTO> enderecos) {
		this.enderecos = enderecos;
	}

}
