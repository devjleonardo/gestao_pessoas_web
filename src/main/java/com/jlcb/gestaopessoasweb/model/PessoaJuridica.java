package com.jlcb.gestaopessoasweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "pessoa_juridica", uniqueConstraints = @UniqueConstraint(name = "uk_pessoa_juridica_cnpj", columnNames = "cnpj"))
@PrimaryKeyJoinColumn(name = "pessoa_id")
public class PessoaJuridica extends Pessoa {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String razaoSocial;

	@Column(nullable = false, unique = true)
	private String cnpj;

	public PessoaJuridica() {
	}

	public PessoaJuridica(String razaoSocial, String cnpj) {
		this.razaoSocial = razaoSocial;
		this.cnpj = cnpj;
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
	
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj) {
//			return true;
//		}
//
//		if (obj == null || getClass() != obj.getClass()) {
//			return false;
//		}
//
//		PessoaJuridica o = (PessoaJuridica) obj;
//
//		return Objects.equals(id, o.id) && Objects.equals(email, o.email) 
//		    && Objects.equals(cnpj, o.cnpj);
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(
//		    Objects.requireNonNull(id, "id não pode ser null"),
//		    Objects.requireNonNull(email, "email não pode ser null"),
//			Objects.requireNonNull(cnpj, "cnpj não pode ser null")
//	    );
//	}

}
