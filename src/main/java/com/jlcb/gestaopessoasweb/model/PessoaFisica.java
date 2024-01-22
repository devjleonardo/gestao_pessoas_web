package com.jlcb.gestaopessoasweb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "pessoa_fisica")
@PrimaryKeyJoinColumn(name = "pessoa_id")
public class PessoaFisica extends Pessoa {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String nome;

	@Column(nullable = false, unique = true)
	private String cpf;
	

	public PessoaFisica() {
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
//		PessoaFisica o = (PessoaFisica) obj;
//
//		return Objects.equals(id, o.id) && Objects.equals(email, o.email) && Objects.equals(cpf, o.cpf);
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(
//		    Objects.requireNonNull(id, "id não pode ser null"),
//			Objects.requireNonNull(email, "email não pode ser null"),
//			Objects.requireNonNull(cpf, "cpf não pode ser null")
//		);
//	}

}
