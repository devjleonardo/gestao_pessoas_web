package com.jlcb.gestaopessoasweb.api.dto.response;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

public class EnderecoResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(example = "1")
	private Long id;

	@Schema(example = "87053028")
	private String cep;

	@Schema(example = "Rua Dolores Duran")
	private String logradouro;

	@Schema(example = "Jardim Paraíso")
	private String bairro;

	@Schema(example = "Apartamento 302")
	private String numero;

	@Schema(example = "Apartamento 302")
	private String complemento;

	@Schema(example = "Maringá")
	private String cidade;

	@Schema(example = "PR")
	private String uf;

	public EnderecoResponseDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

}
