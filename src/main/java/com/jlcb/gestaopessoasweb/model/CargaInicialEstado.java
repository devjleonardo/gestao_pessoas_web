package com.jlcb.gestaopessoasweb.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
	
@Entity
@Table(name = "carga_inicial_estado")
@SequenceGenerator(name = "seq_carga_inicial_estado", sequenceName = "seq_carga_inicial_estado", allocationSize = 1, initialValue = 1)
public class CargaInicialEstado implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_carga_inicial_estado")
	protected Long id;

	private boolean cargaInicialExecutada;

	public CargaInicialEstado() {
	}
	
	public CargaInicialEstado(boolean cargaInicialExecutada) {
		this.cargaInicialExecutada = cargaInicialExecutada;
	}

	public Long getId() {
		return id;
	}

	public boolean isCargaInicialExecutada() {
		return cargaInicialExecutada;
	}

	public void setCargaInicialExecutada(boolean cargaInicialExecutada) {
		this.cargaInicialExecutada = cargaInicialExecutada;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		CargaInicialEstado o = (CargaInicialEstado) obj;

		return Objects.equals(id, o.id);
	}

	@Override
	public int hashCode() {
	    return Objects.hash(id);
	}

}