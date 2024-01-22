package com.jlcb.gestaopessoasweb.api.exception;

import java.io.Serializable;
import java.util.Date;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ApiExceptionResponse")
public class ApiExceptionResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(example = "2024-01-20T10:02:12.286+00:00")
	private Date timestamp;
	
	@Schema(example = "Não existe um cadastro de pessoa física com o código 10066")
	private String message;
	
	@Schema(example = "uri=/pessoas-fisicas/10066")
	private String details;

	public ApiExceptionResponse(Date timestamp, String message, String details) {
		this.timestamp = timestamp;
		this.message = message;
		this.details = details;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public String getMessage() {
		return message;
	}

	public String getDetails() {
		return details;
	}

}