package com.jlcb.gestaopessoasweb.exception;

public class PessoaJuridicaNaoEncontradaException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;

	public PessoaJuridicaNaoEncontradaException(String mensagem) {
		super(mensagem);
	}
	
	public PessoaJuridicaNaoEncontradaException(Long idPessoaFisica) {
		this(String.format("Não existe um cadastro de pessoa física com o código %d", idPessoaFisica));
	}

}
