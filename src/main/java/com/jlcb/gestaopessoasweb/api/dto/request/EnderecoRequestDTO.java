package com.jlcb.gestaopessoasweb.api.dto.request;

import java.io.Serializable;

import com.jlcb.gestaopessoasweb.model.Pessoa;

public class EnderecoRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;

	private String cep;

	private String logradouro;

	private String bairro;

	private String numero;

	private String complemento;

	private String cidade;

	private String uf;

	private Pessoa pessoa;

	public EnderecoRequestDTO() {
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
        if (complemento != null && !complemento.isEmpty()) {
        	this.complemento = complemento;
        }
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

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

}
