package com.jlcb.gestaopessoasweb.api.dto.request;

import java.io.Serializable;
import java.util.List;

public class PessoaJuridicaRequestDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private String razaoSocial;

	private String cnpj;

	private String email;

	private List<EnderecoRequestDTO> enderecos;

	public PessoaJuridicaRequestDTO() {
	}

	public PessoaJuridicaRequestDTO(String razaoSocial, String cnpj, String email, List<EnderecoRequestDTO> enderecos) {
		this.razaoSocial = razaoSocial;
		this.cnpj = cnpj;
		this.email = email;
		this.enderecos = enderecos;
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

	public List<EnderecoRequestDTO> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<EnderecoRequestDTO> enderecos) {
		this.enderecos = enderecos;
	}

}
