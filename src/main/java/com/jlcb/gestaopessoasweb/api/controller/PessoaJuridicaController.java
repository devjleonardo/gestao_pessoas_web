package com.jlcb.gestaopessoasweb.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jlcb.gestaopessoasweb.api.dto.request.PessoaJuridicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaJuridicaResponseDTO;
import com.jlcb.gestaopessoasweb.api.openapi.controller.PessoaJuridicaControllerOpenApi;
import com.jlcb.gestaopessoasweb.service.PessoaJuridicaService;

@RestController
@RequestMapping("/pessoas-juridicas")
public class PessoaJuridicaController implements PessoaJuridicaControllerOpenApi {

	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@GetMapping
	public List<PessoaJuridicaResponseDTO> listar() {
		List<PessoaJuridicaResponseDTO> todasPessoasJuridicas = pessoaJuridicaService.listar();

		return todasPessoasJuridicas;
	}

	@GetMapping("/{id}")
	public ResponseEntity<PessoaJuridicaResponseDTO> obterPorId(@PathVariable Long id) {
		PessoaJuridicaResponseDTO pessoaJuridica = pessoaJuridicaService.obterOuFalharPorId(id);

		return ResponseEntity.ok(pessoaJuridica);
	}

	@PostMapping
	public ResponseEntity<PessoaJuridicaResponseDTO> criar(
			@RequestBody PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO) {
		PessoaJuridicaResponseDTO pessoaJuridicaCriada = pessoaJuridicaService.criar(pessoaJuridicaRequestDTO);
		return new ResponseEntity<>(pessoaJuridicaCriada, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PessoaJuridicaResponseDTO> atualizar(@PathVariable Long id, @RequestBody PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO) {
	    PessoaJuridicaResponseDTO pessoaJuridicaAtualizada = pessoaJuridicaService.atualizar(id, pessoaJuridicaRequestDTO);
	    return ResponseEntity.ok(pessoaJuridicaAtualizada);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirPorId(@PathVariable Long id) {
		pessoaJuridicaService.excluirPorId(id);

		return ResponseEntity.noContent().build();
	}

}
