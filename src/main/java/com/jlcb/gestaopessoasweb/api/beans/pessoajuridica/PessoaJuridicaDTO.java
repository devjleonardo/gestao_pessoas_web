package com.jlcb.gestaopessoasweb.api.beans.pessoajuridica;

import java.io.Serializable;

public class PessoaJuridicaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String razaoSocial;

	private String cnpj;

	private String email;

	public PessoaJuridicaDTO() {
	}

	public PessoaJuridicaDTO(Long id, String razaoSocial, String cnpj, String email) {
		this.id = id;
		this.razaoSocial = razaoSocial;
		this.cnpj = cnpj;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
