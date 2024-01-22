package com.jlcb.gestaopessoasweb.api.exception;

import java.util.Date;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.jlcb.gestaopessoasweb.exception.EntidadeEmUsoException;
import com.jlcb.gestaopessoasweb.exception.PessoaJuridicaNaoEncontradaException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(PessoaJuridicaNaoEncontradaException.class)
	public ResponseEntity<ApiExceptionResponse> handlePessoaFisicaNaoEncontradaException(
	        PessoaJuridicaNaoEncontradaException ex, WebRequest request) {

		ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
			new Date(), ex.getMessage(), request.getDescription(false)
		);
		
		return new ResponseEntity<>(apiExceptionResponse, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<ApiExceptionResponse> handleEntidadeEmUsoException(
			EntidadeEmUsoException ex, WebRequest request) {

		ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
			new Date(), ex.getMessage(), request.getDescription(false)
		);
		
		return new ResponseEntity<>(apiExceptionResponse, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<?> handleDataIntegrityViolation(
	        DataIntegrityViolationException ex, WebRequest request) {
	    Throwable rootCause = ExceptionUtils.getRootCause(ex);
	    
	    if (rootCause.getMessage().contains("duplicate key value violates unique constraint")) {
	    	return handleCampoDuplicado(rootCause.getMessage(), request);
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro interno do servidor");
	    }
	}
	
	private ResponseEntity<ApiExceptionResponse> handleCampoDuplicado(String message, WebRequest request) {
	    String nomeDoCampo = extrairNomeDoCampo(message);
	
	    ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
	        new Date(), construirMensagemErroCampoDuplicado(nomeDoCampo.toUpperCase()), request.getDescription(false)
	    );
	
	    return new ResponseEntity<>(apiExceptionResponse, HttpStatus.CONFLICT);
	}

    private String extrairNomeDoCampo(String message) {
        int startIndex = message.indexOf("(") + 1;
        int endIndex = message.indexOf(")");
        
        return message.substring(startIndex, endIndex);
    }
    
    private String construirMensagemErroCampoDuplicado(String fieldName) {
        return "Já existe um cadastro com esse valor fornecido no campo " + fieldName + ". "
             + "Por favor, insira um outro valor.";
    }

}
