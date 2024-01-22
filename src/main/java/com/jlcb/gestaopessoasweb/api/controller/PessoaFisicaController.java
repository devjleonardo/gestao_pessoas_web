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

import com.jlcb.gestaopessoasweb.api.dto.request.PessoaFisicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaFisicaResponseDTO;
import com.jlcb.gestaopessoasweb.api.openapi.controller.PessoaFisicaControllerOpenApi;
import com.jlcb.gestaopessoasweb.service.PessoaFisicaService;

@RestController
@RequestMapping("/pessoas-fisicas")
public class PessoaFisicaController implements PessoaFisicaControllerOpenApi {

	@Autowired
	private PessoaFisicaService pessoaFisicaService;

	@GetMapping
	public List<PessoaFisicaResponseDTO> listar() {
		List<PessoaFisicaResponseDTO> todasPessoasFisicas = pessoaFisicaService.listar();

		return todasPessoasFisicas;
	}

	@GetMapping("/{id}")
	public ResponseEntity<PessoaFisicaResponseDTO> obterPorId(@PathVariable Long id) {
		PessoaFisicaResponseDTO pessoaFisica = pessoaFisicaService.obterOuFalharPorId(id);

		return ResponseEntity.ok(pessoaFisica);
	}

	@PostMapping
	public ResponseEntity<PessoaFisicaResponseDTO> criar(@RequestBody PessoaFisicaRequestDTO pessoaFisicaRequestDTO) {
		PessoaFisicaResponseDTO pessoaFisicaCriada = pessoaFisicaService.criar(pessoaFisicaRequestDTO);
		return new ResponseEntity<>(pessoaFisicaCriada, HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PessoaFisicaResponseDTO> atualizar(@PathVariable Long id, @RequestBody PessoaFisicaRequestDTO pessoaFisicaRequestDTO) {
	    PessoaFisicaResponseDTO pessoaFisicaAtualizada = pessoaFisicaService.atualizar(id, pessoaFisicaRequestDTO);
	    return ResponseEntity.ok(pessoaFisicaAtualizada);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirPorId(@PathVariable Long id) {
		pessoaFisicaService.excluirPorId(id);

		return ResponseEntity.noContent().build();
	}

}
