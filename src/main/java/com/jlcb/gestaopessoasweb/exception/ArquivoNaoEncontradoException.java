package com.jlcb.gestaopessoasweb.exception;

public class ArquivoNaoEncontradoException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ArquivoNaoEncontradoException() {
        super();
    }

    public ArquivoNaoEncontradoException(String message) {
        super(message);
    }

    public ArquivoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
    
}