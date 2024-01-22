package com.jlcb.gestaopessoasweb.service.integracao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.github.javafaker.Faker;
import com.jlcb.gestaopessoasweb.api.dto.request.PessoaFisicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaFisicaResponseDTO;
import com.jlcb.gestaopessoasweb.exception.PessoaJuridicaNaoEncontradaException;
import com.jlcb.gestaopessoasweb.model.PessoaFisica;
import com.jlcb.gestaopessoasweb.repository.PessoaFisicaRepository;
import com.jlcb.gestaopessoasweb.service.PessoaFisicaService;

@SpringBootTest
@ActiveProfiles("test")
public class PessoaFisicaServiceIT {
	
    @Autowired
    private PessoaFisicaService pessoaFisicaService;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;
    
    @BeforeEach
    void setUp() {
        pessoaFisicaRepository.deleteAll();
    }
    
    @Test
    void listarDeveRetornarListaDePessoasFisicasQuandoElasExistirem() {
        PessoaFisica pessoaFisica = criarPessoaFisicaNoBanco();
        
        List<PessoaFisicaResponseDTO> listaPessoasFisicas = pessoaFisicaService.listar();
        
        assertFalse(listaPessoasFisicas.isEmpty());
        
        assertEquals(1, listaPessoasFisicas.size());
        
        assertTrue(
            listaPessoasFisicas.stream().anyMatch(
        	    dto -> dto.getId().equals(pessoaFisica.getId())
        	)
        );
    }
    
    @Test
    void testListarDeveRetornarListaVaziaQuandoNaoExistiremPessoasFisicas() {
        List<PessoaFisicaResponseDTO> listaPessoasFisicas = pessoaFisicaService.listar();

        assertEquals(0, listaPessoasFisicas.size());
    }
    
    @Test
    void obterOuFalharPorIdDeveRetornarDTOQuandoPessoaFisicaExistir() {
        PessoaFisica pessoaFisica = criarPessoaFisicaNoBanco();

        Long idPessoaFisica = pessoaFisica.getId();
        
        PessoaFisicaResponseDTO pessoaFisicaResponseDTO = 
            pessoaFisicaService.obterOuFalharPorId(idPessoaFisica);
    
        assertNotNull(pessoaFisicaResponseDTO);
        
        assertEquals(pessoaFisica.getId(), pessoaFisicaResponseDTO.getId());
        assertEquals(pessoaFisica.getNome(), pessoaFisicaResponseDTO.getNome());
        assertEquals(pessoaFisica.getCpf(), pessoaFisicaResponseDTO.getCpf());
        assertEquals(pessoaFisica.getEmail(), pessoaFisicaResponseDTO.getEmail());
    }
    
    @Test
    void obterOuFalharPorIdDeveLancarExcecaoQuandoPessoaFisicaNaoExistir() {
        assertThrows(PessoaJuridicaNaoEncontradaException.class, () -> {
            pessoaFisicaService.obterOuFalharPorId(86988L);
        });
    }
    
    @Test
    void criarPessoaFisicaDeveRetornarDTO() {
    	PessoaFisicaRequestDTO pessoaFisicaRequestDTO = criarPessoaFisicaRequestDTO();

        PessoaFisicaResponseDTO pessoaFisicaResponseDTO = 
            pessoaFisicaService.criar(pessoaFisicaRequestDTO);

        assertNotNull(pessoaFisicaResponseDTO.getId());
        assertEquals(pessoaFisicaRequestDTO.getNome(), pessoaFisicaResponseDTO.getNome());
        assertEquals(pessoaFisicaRequestDTO.getCpf(), pessoaFisicaResponseDTO.getCpf());
        assertEquals(pessoaFisicaRequestDTO.getEmail(), pessoaFisicaResponseDTO.getEmail());
    }
    
    @Test
    void excluirPorIdDeveExcluirPessoaFisicaQuandoExistir() {
        PessoaFisica pessoaFisica = criarPessoaFisicaNoBanco();

        pessoaFisicaService.excluirPorId(pessoaFisica.getId());

        assertFalse(pessoaFisicaRepository.existsById(pessoaFisica.getId()));
    }
    
    private PessoaFisicaRequestDTO criarPessoaFisicaRequestDTO() {
    	Faker faker = new Faker();
    	
        PessoaFisicaRequestDTO pessoaFisicaRequestDTO = new PessoaFisicaRequestDTO();
        pessoaFisicaRequestDTO.setNome(faker.name().fullName());
        pessoaFisicaRequestDTO.setCpf(faker.number().digits(11));
        pessoaFisicaRequestDTO.setEmail(faker.internet().emailAddress());
        
        return pessoaFisicaRequestDTO;
    }

    private PessoaFisica criarPessoaFisicaNoBanco() {
    	Faker faker = new Faker();
    	
        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setNome(faker.name().fullName());
        pessoaFisica.setCpf(faker.number().digits(11));
        pessoaFisica.setEmail(faker.internet().emailAddress());
        
        return pessoaFisicaRepository.save(pessoaFisica);
    }

}
