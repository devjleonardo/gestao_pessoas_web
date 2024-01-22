package com.jlcb.gestaopessoasweb.api.dto.request;

import java.io.Serializable;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public class PessoaFisicaRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(example = "Ana Silva Santos", required = true)
    private String nome;

	@Schema(example = "12345678909", required = true)
    private String cpf;

	@Schema(example = "ana.silva@email.com", required = true)
    private String email;

	private List<EnderecoRequestDTO> enderecos;

	public PessoaFisicaRequestDTO() {
	}

	public PessoaFisicaRequestDTO(String nome, String cpf, String email) {
		this.nome = nome;
		this.cpf = cpf;
		this.email = email;
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

	public List<EnderecoRequestDTO> getEnderecos() {
		return enderecos;
	}

	public void adicionarEndereco(EnderecoRequestDTO endereco) {
		enderecos.add(endereco);
	}

}
