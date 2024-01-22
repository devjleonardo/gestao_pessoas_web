package com.jlcb.gestaopessoasweb.api.openapi.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.jlcb.gestaopessoasweb.api.dto.request.PessoaFisicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaFisicaResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@SecurityRequirement(name = "security_auth")
@Tag(name = "Pessoas Físicas")
public interface PessoaFisicaControllerOpenApi {

    @Operation(summary = "Lista as pessoas físicas")
    List <PessoaFisicaResponseDTO> listar();

    @Operation(
        summary = "Busca uma pessoa por Id",
        responses = {
            @ApiResponse(responseCode = "200"),

            @ApiResponse(
                responseCode = "400",
                description = "ID da pessoa física inválido",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            ),

            @ApiResponse(
                responseCode = "404",
                description = "Pessoa física não encontrada",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            )
        }
    )
    ResponseEntity <PessoaFisicaResponseDTO> obterPorId(
        @Parameter(description = "ID de uma pessoa física", example = "1", required = true) 
        Long id
    );

    @Operation(
        summary = "Cadastra uma pessoa física",
        responses = {
            @ApiResponse(responseCode = "200"),

            @ApiResponse(
                responseCode = "409",
                description = "Já existe pessoa física com esses dados",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            )
        }
    )
    ResponseEntity <PessoaFisicaResponseDTO> criar(
        @RequestBody(description = "Representação de uma nova pesso física", required = true) 
        PessoaFisicaRequestDTO pessoaFisicaRequestDTO
    );

    @Operation(
        summary = "Atualizado uma pessoa física por ID",
        responses = {
            @ApiResponse(responseCode = "200"),

            @ApiResponse(
                responseCode = "400",
                description = "ID da pessoa física inválido",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            ),

            @ApiResponse(
                responseCode = "404",
                description = "Pessoa física não encontrada",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            )
        }

    )
    ResponseEntity <PessoaFisicaResponseDTO> atualizar(
        @Parameter(description = "ID de uma pessoa física", example = "1", required = true) 
        Long id,

        @RequestBody(
            description = "Representação de uma pessoa física com dados atualizados",
            required = true
        ) PessoaFisicaRequestDTO pessoaFisicaRequestDTO
    );

    @Operation(
        summary = "Excluir uma pessoa física por ID",
        responses = {
            @ApiResponse(responseCode = "204"),

            @ApiResponse(
                responseCode = "400",
                description = "ID da pessoa física inválido",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            ),

            @ApiResponse(
                responseCode = "404",
                description = "Pessoa física não encontrada",
                content = @Content(schema = @Schema(ref = "ApiExceptionResponse"))
            )
        }
    )
    ResponseEntity <Void> excluirPorId(
        @Parameter(description = "ID de uma pessoa física", example = "1", required = true) 
        Long cidadeId
    );

}