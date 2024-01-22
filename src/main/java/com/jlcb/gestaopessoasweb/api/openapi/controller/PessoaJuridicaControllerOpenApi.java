package com.jlcb.gestaopessoasweb.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jlcb.gestaopessoasweb.api.dto.request.PessoaJuridicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaJuridicaResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Pessoas Jurídicas")
public interface PessoaJuridicaControllerOpenApi {

    @Operation(summary = "Lista as pessoas jurídicas")
    List <PessoaJuridicaResponseDTO> listar();

    @Operation(
        summary = "Busca uma pessoa jurídica por Id",
        responses = {
            @ApiResponse(responseCode = "200"),

            @ApiResponse(
                responseCode = "400",
                description = "ID da pessoa jurídica inválido",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            ),

            @ApiResponse(
                responseCode = "404",
                description = "Pessoa jurídica não encontrada",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            )
        }
    )
    ResponseEntity <PessoaJuridicaResponseDTO> obterPorId(
        @Parameter(description = "ID de uma pessoa jurídica", example = "1", required = true) 
        Long id
    );

    @Operation(
        summary = "Cadastra uma pessoa jurídica",
        responses = {
            @ApiResponse(responseCode = "200"),

            @ApiResponse(
                responseCode = "409",
                description = "Já existe pessoa jurídica com esses dados",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            )
        }
    )
    ResponseEntity <PessoaJuridicaResponseDTO> criar(
        @RequestBody(description = "Representação de uma nova pesso jurídica", required = true) 
        PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO
    );

    @Operation(
        summary = "Atualizado uma pessoa jurídica por ID",
        responses = {
            @ApiResponse(responseCode = "200"),

            @ApiResponse(
                responseCode = "400",
                description = "ID da pessoa jurídica inválido",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            ),

            @ApiResponse(
                responseCode = "404",
                description = "Pessoa jurídica não encontrada",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            )
        }

    )
    ResponseEntity <PessoaJuridicaResponseDTO> atualizar(
        @Parameter(description = "ID de uma pessoa jurídica", example = "1", required = true) 
        Long id,

        @RequestBody(
            description = "Representação de uma pessoa jurídica com dados atualizados",
            required = true
        ) 
        PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO
    );

    @Operation(
        summary = "Excluir uma pessoa jurídica por ID",
        responses = {
            @ApiResponse(responseCode = "204"),

            @ApiResponse(
                responseCode = "400",
                description = "ID da pessoa jurídica inválido",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            ),

            @ApiResponse(
                responseCode = "404",
                description = "Pessoa jurídica não encontrada",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            )
        }
    )
    ResponseEntity <Void> excluirPorId(
        @Parameter(description = "ID de uma pessoa jurídica", example = "1", required = true) 
        Long cidadeId
    );

}