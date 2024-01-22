package com.jlcb.gestaopessoasweb.api.dto.response;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

public class PessoaJuridicaResponseDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(example = "1002")
	private Long id;

	@Schema(example = "Empresa ABC Ltda")
	private String razaoSocial;

	@Schema(example = "12345678000190")
	private String cnpj;

	@Schema(example = "contato@empresaabc.com")
	private String email;

	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<EnderecoResponseDTO> enderecos;

	public PessoaJuridicaResponseDTO() {
	}

	public PessoaJuridicaResponseDTO(Long id, String razaoSocial, String cnpj, String email) {
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

	public List<EnderecoResponseDTO> getEnderecos() {
		return enderecos;
	}

	public void setEnderecos(List<EnderecoResponseDTO> enderecos) {
		this.enderecos = enderecos;
	}

}
