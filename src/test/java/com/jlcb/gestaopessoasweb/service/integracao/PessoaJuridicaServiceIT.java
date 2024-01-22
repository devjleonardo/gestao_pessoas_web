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
import com.jlcb.gestaopessoasweb.api.dto.request.PessoaJuridicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaJuridicaResponseDTO;
import com.jlcb.gestaopessoasweb.exception.PessoaJuridicaNaoEncontradaException;
import com.jlcb.gestaopessoasweb.model.PessoaJuridica;
import com.jlcb.gestaopessoasweb.repository.PessoaJuridicaRepository;
import com.jlcb.gestaopessoasweb.service.PessoaJuridicaService;

@SpringBootTest
@ActiveProfiles("test")
public class PessoaJuridicaServiceIT {
	
    @Autowired
    private PessoaJuridicaService pessoaJuridicaService;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;
    
    @BeforeEach
    void setUp() {
        pessoaJuridicaRepository.deleteAll();
    }
    
    @Test
    void listarDeveRetornarListaDePessoasJuridicasQuandoElasExistirem() {
        PessoaJuridica pessoaJuridica = criarPessoaJuridicaNoBanco();
        
        List<PessoaJuridicaResponseDTO> listaPessoasJuridicas = pessoaJuridicaService.listar();
        
        assertFalse(listaPessoasJuridicas.isEmpty());
        
        assertEquals(1, listaPessoasJuridicas.size());
        
        assertTrue(
            listaPessoasJuridicas.stream().anyMatch(
        	    dto -> dto.getId().equals(pessoaJuridica.getId())
        	)
        );
    }
    
    @Test
    void testListarDeveRetornarListaVaziaQuandoNaoExistiremPessoasJuridicas() {
        List<PessoaJuridicaResponseDTO> listaPessoasJuridicas = pessoaJuridicaService.listar();

        assertEquals(0, listaPessoasJuridicas.size());
    }
    
    @Test
    void obterOuFalharPorIdDeveRetornarDTOQuandoPessoaJuridicaExistir() {
        PessoaJuridica pessoaJuridica = criarPessoaJuridicaNoBanco();

        Long idPessoaJuridica = pessoaJuridica.getId();
        
        PessoaJuridicaResponseDTO pessoaJuridicaResponseDTO = 
            pessoaJuridicaService.obterOuFalharPorId(idPessoaJuridica);
    
        assertNotNull(pessoaJuridicaResponseDTO);
        
        assertEquals(pessoaJuridica.getId(), pessoaJuridicaResponseDTO.getId());
        assertEquals(pessoaJuridica.getRazaoSocial(), pessoaJuridicaResponseDTO.getRazaoSocial());
        assertEquals(pessoaJuridica.getCnpj(), pessoaJuridicaResponseDTO.getCnpj());
        assertEquals(pessoaJuridica.getEmail(), pessoaJuridicaResponseDTO.getEmail());
    }
    
    @Test
    void obterOuFalharPorIdDeveLancarExcecaoQuandoPessoaJuridicaNaoExistir() {
        assertThrows(PessoaJuridicaNaoEncontradaException.class, () -> {
            pessoaJuridicaService.obterOuFalharPorId(12742L);
        });
    }
    
    @Test
    void criarPessoaJuridicaDeveRetornarDTO() {
    	PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO = criarPessoaJuridicaRequestDTO();

    	PessoaJuridicaResponseDTO pessoaFisicaResponseDTO = 
            pessoaJuridicaService.criar(pessoaJuridicaRequestDTO);

        assertNotNull(pessoaFisicaResponseDTO.getId());
        assertEquals(pessoaJuridicaRequestDTO.getRazaoSocial(), pessoaFisicaResponseDTO.getRazaoSocial());
        assertEquals(pessoaJuridicaRequestDTO.getCnpj(), pessoaFisicaResponseDTO.getCnpj());
        assertEquals(pessoaJuridicaRequestDTO.getEmail(), pessoaFisicaResponseDTO.getEmail());
    }
    
    @Test
    void excluirPorIdDeveExcluirPessoaJuridicaQuandoExistir() {
        PessoaJuridica pessoaJuridica = criarPessoaJuridicaNoBanco();

        pessoaJuridicaService.excluirPorId(pessoaJuridica.getId());

        assertFalse(pessoaJuridicaRepository.existsById(pessoaJuridica.getId()));
    }
    
    private PessoaJuridicaRequestDTO criarPessoaJuridicaRequestDTO() {
    	Faker faker = new Faker();
    	
        PessoaJuridicaRequestDTO pessoaFisicaRequestDTO = new PessoaJuridicaRequestDTO();
        pessoaFisicaRequestDTO.setRazaoSocial(faker.name().fullName());
        pessoaFisicaRequestDTO.setCnpj(faker.number().digits(14));
        pessoaFisicaRequestDTO.setEmail(faker.internet().emailAddress());
        
        return pessoaFisicaRequestDTO;
    }

    private PessoaJuridica criarPessoaJuridicaNoBanco() {
    	Faker faker = new Faker();
    	
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setRazaoSocial(faker.name().fullName());
        pessoaJuridica.setCnpj(faker.number().digits(14));
        pessoaJuridica.setEmail(faker.internet().emailAddress());
        
        return pessoaJuridicaRepository.save(pessoaJuridica);
    }

}
