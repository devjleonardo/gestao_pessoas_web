package com.jlcb.gestaopessoasweb.service.unitario;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.jlcb.gestaopessoasweb.api.dto.request.PessoaFisicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaFisicaResponseDTO;
import com.jlcb.gestaopessoasweb.exception.EntidadeEmUsoException;
import com.jlcb.gestaopessoasweb.exception.PessoaJuridicaNaoEncontradaException;
import com.jlcb.gestaopessoasweb.model.PessoaFisica;
import com.jlcb.gestaopessoasweb.repository.PessoaFisicaRepository;
import com.jlcb.gestaopessoasweb.service.PessoaFisicaService;

@SpringBootTest
public class PessoaFisicaServiceTest {
	
    @Mock
    private PessoaFisicaRepository pessoaFisicaRepository;

    @InjectMocks
    private PessoaFisicaService pessoaFisicaService;
    
    private PessoaFisica pessoaFisicaPadrao;
    
    private PessoaFisicaRequestDTO pessoaFisicaRequestDTOPadrao;

    @BeforeEach
    void setUp() {
        pessoaFisicaPadrao = criarPessoaFisicaPadrao();
        pessoaFisicaRequestDTOPadrao = criarPessoaFisicaRequestDTOPadrao();
    }
    
    @Test
    void listarDeveRetornarListaDePessoasFisicasQuandoElasExistirem() {
        when(pessoaFisicaRepository.findAll()).thenReturn(
            Arrays.asList(pessoaFisicaPadrao)
        );

        List<PessoaFisicaResponseDTO> lista = pessoaFisicaService.listar();

        assertEquals(1, lista.size());
    }

    @Test
    void listarDeveRetornarListaVaziaQuandoNaoExistiremPessoasFisicas() {
        when(pessoaFisicaRepository.findAll()).thenReturn(Collections.emptyList());

        List<PessoaFisicaResponseDTO> lista = pessoaFisicaService.listar();

        assertEquals(0, lista.size());
    }
    
    @Test
    void obterOuFalharPorIdDeveRetornarDTOQuandoPessoaFisicaExistir() {
        Long idPessoaFisica = 1L;
        
        when(pessoaFisicaRepository.buscarComEnderecosPorId(idPessoaFisica)).thenReturn(
            Optional.of(pessoaFisicaPadrao)
        );

        PessoaFisicaResponseDTO pessoaFisicaResponseDTO = 
        	pessoaFisicaService.obterOuFalharPorId(idPessoaFisica);

        assertPessoaFisicaResponseDTOEqualsEntidade(pessoaFisicaResponseDTO, pessoaFisicaPadrao);
    }
    
    @Test
    void obterOuFalharPorIdDeveLancarExcecaoQuandoPessoaFisicaNaoExistir() {
        Long idPessoaFisicaInexistente = 86988L;

        when(pessoaFisicaRepository.findById(idPessoaFisicaInexistente)).thenReturn(
            Optional.empty()
        );

        assertThrows(PessoaJuridicaNaoEncontradaException.class, () -> {
            pessoaFisicaService.obterOuFalharPorId(idPessoaFisicaInexistente);
        });
    }
    
    @Test
    void criarPessoaFisicaDeveRetornarDTO() {
        when(pessoaFisicaRepository.save(any(PessoaFisica.class))).thenReturn(pessoaFisicaPadrao);

        PessoaFisicaResponseDTO pessoaFisicaResponseDTO = 
            pessoaFisicaService.criar(pessoaFisicaRequestDTOPadrao);
        	
        assertPessoaFisicaResponseDTOEqualsEntidade(pessoaFisicaResponseDTO, pessoaFisicaPadrao);
    }
    
    @Test
    void criarPessoaFisicaDeveConverterDTOParaEntidadeAoSalvar() {
        when(pessoaFisicaRepository.save(any(PessoaFisica.class))).thenAnswer(invocation -> {
            PessoaFisica pessoaFisicaSalva = invocation.getArgument(0);
            assertEquals(pessoaFisicaRequestDTOPadrao.getNome(), pessoaFisicaSalva.getNome());
            assertEquals(pessoaFisicaRequestDTOPadrao.getCpf(), pessoaFisicaSalva.getCpf());
            assertEquals(pessoaFisicaRequestDTOPadrao.getEmail(), pessoaFisicaSalva.getEmail());
            
            return pessoaFisicaSalva;
        });

        pessoaFisicaService.criar(pessoaFisicaRequestDTOPadrao);
    }
    
    @Test
    void criarPessoaFisicaDeveLancarExcecaoQuandoFalharAoSalvarPessoaFisica() {
        when(pessoaFisicaRepository.save(any(PessoaFisica.class)))
            .thenThrow(new RuntimeException("Erro ao salvar pessoa física"));

        assertThrows(RuntimeException.class, () -> {
            pessoaFisicaService.criar(pessoaFisicaRequestDTOPadrao);
        });
    }
    
    @Test
    void excluirPorIdDeveExcluirPessoaFisicaQuandoExistir() {
        Long idPessoaFisica = 1L;
        
        when(pessoaFisicaRepository.existsById(idPessoaFisica)).thenReturn(true);

        pessoaFisicaService.excluirPorId(idPessoaFisica);

        verify(pessoaFisicaRepository, times(1)).deleteById(idPessoaFisica);
        verify(pessoaFisicaRepository, times(1)).flush();
    }
    
    @Test
    void excluirPorIdDeveLancarExcecaoQuandoPessoaFisicaNaoExistir() {
        Long idPessoaFisica = 86988L;
        
        when(pessoaFisicaRepository.existsById(idPessoaFisica)).thenReturn(false);

        assertThrows(
          PessoaJuridicaNaoEncontradaException.class, 
          () -> pessoaFisicaService.excluirPorId(idPessoaFisica)
        );
    }
    
    @Test
    void excluirPorIdDeveLancarEntidadeEmUsoExceptionQuandoDataIntegrityViolationExceptionOcorrer() {
        Long idPessoaFisica = 86988L;
        
        when(pessoaFisicaRepository.existsById(idPessoaFisica)).thenReturn(true);
        
        doThrow(DataIntegrityViolationException.class).when(pessoaFisicaRepository)
            .deleteById(idPessoaFisica);

        assertThrows(
            EntidadeEmUsoException.class, 
            () -> pessoaFisicaService.excluirPorId(idPessoaFisica)
        );
    }
    
    private void assertPessoaFisicaResponseDTOEqualsEntidade(
    		PessoaFisicaResponseDTO dto, PessoaFisica entidade) {
        assertEquals(dto.getId(), entidade.getId());
        assertEquals(dto.getNome(), entidade.getNome());
        assertEquals(dto.getCpf(), entidade.getCpf());
        assertEquals(dto.getEmail(), entidade.getEmail());
    }
    
    public PessoaFisica criarPessoaFisicaPadrao() {
        PessoaFisica pessoaFisica = new PessoaFisica();
        pessoaFisica.setId(1L);
        pessoaFisica.setNome("Renata Daiane Assis");
        pessoaFisica.setCpf("29133232946");
        pessoaFisica.setEmail("renata.daiane.assis@lumavale.com.br");
        
        return pessoaFisica;
    }
    
	public PessoaFisicaRequestDTO criarPessoaFisicaRequestDTOPadrao() {
		PessoaFisicaRequestDTO pessoaFisicaRequestDTO = new PessoaFisicaRequestDTO();
		pessoaFisicaRequestDTO.setNome("Renata Daiane Assis");
		pessoaFisicaRequestDTO.setCpf("29133232946");
		pessoaFisicaRequestDTO.setEmail("renata.daiane.assis@lumavale.com.br");
		
		return pessoaFisicaRequestDTO;
	}
    
}
