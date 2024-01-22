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

import com.jlcb.gestaopessoasweb.api.dto.request.PessoaJuridicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaJuridicaResponseDTO;
import com.jlcb.gestaopessoasweb.exception.EntidadeEmUsoException;
import com.jlcb.gestaopessoasweb.exception.PessoaJuridicaNaoEncontradaException;
import com.jlcb.gestaopessoasweb.model.PessoaJuridica;
import com.jlcb.gestaopessoasweb.repository.PessoaJuridicaRepository;
import com.jlcb.gestaopessoasweb.service.PessoaJuridicaService;

@SpringBootTest
public class PessoaJuridicaServiceTest {
	
    @Mock
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @InjectMocks
    private PessoaJuridicaService pessoaJuridicaService;
    
    private PessoaJuridica pessoaJuridicaPadrao;
    
    private PessoaJuridicaRequestDTO pessoaJuridicaRequestDTOPadrao;

    @BeforeEach
    void setUp() {
        pessoaJuridicaPadrao = criarPessoaJuridicaPadrao();
        pessoaJuridicaRequestDTOPadrao = criarPessoaJuridicaRequestDTOPadrao();
    }
    
    @Test
    void listarDeveRetornarListaDePessoasJuridicasQuandoElasExistirem() {
        when(pessoaJuridicaRepository.findAll()).thenReturn(
            Arrays.asList(pessoaJuridicaPadrao)
        );

        List<PessoaJuridicaResponseDTO> lista = pessoaJuridicaService.listar();

        assertEquals(1, lista.size());
    }

    @Test
    void listarDeveRetornarListaVaziaQuandoNaoExistiremPessoasJuridicas() {
        when(pessoaJuridicaRepository.findAll()).thenReturn(Collections.emptyList());

        List<PessoaJuridicaResponseDTO> lista = pessoaJuridicaService.listar();

        assertEquals(0, lista.size());
    }
    
    @Test
    void obterOuFalharPorIdDeveRetornarDTOQuandoPessoaJuridicaExistir() {
        Long idPessoaJuridica = 94L;
        
        when(pessoaJuridicaRepository.buscarComEnderecosPorId(idPessoaJuridica)).thenReturn(
            Optional.of(pessoaJuridicaPadrao)
        );

        PessoaJuridicaResponseDTO pessoaJuridicaResponseDTO = 
        	pessoaJuridicaService.obterOuFalharPorId(idPessoaJuridica);

        assertPessoaJuridicaResponseDTOEqualsEntidade(pessoaJuridicaResponseDTO, pessoaJuridicaPadrao);
    }
    
    @Test
    void obterOuFalharPorIdDeveLancarExcecaoQuandoPessoaJuridicaNaoExistir() {
        Long idPessoaJuridicaInexistente = 12742L;

        when(pessoaJuridicaRepository.findById(idPessoaJuridicaInexistente)).thenReturn(
            Optional.empty()
        );

        assertThrows(PessoaJuridicaNaoEncontradaException.class, () -> {
            pessoaJuridicaService.obterOuFalharPorId(idPessoaJuridicaInexistente);
        });
    }
    
    @Test
    void criarPessoaJuridicaDeveRetornarDTO() {
        when(pessoaJuridicaRepository.save(any(PessoaJuridica.class))).thenReturn(pessoaJuridicaPadrao);

        PessoaJuridicaResponseDTO pessoaJuridicaResponseDTO = 
            pessoaJuridicaService.criar(pessoaJuridicaRequestDTOPadrao);
        	
        assertPessoaJuridicaResponseDTOEqualsEntidade(pessoaJuridicaResponseDTO, pessoaJuridicaPadrao);
    }
    
    @Test
    void criarPessoaJuridicaDeveConverterDTOParaEntidadeAoSalvar() {
        when(pessoaJuridicaRepository.save(any(PessoaJuridica.class))).thenAnswer(invocation -> {
            PessoaJuridica pessoaJuridica = invocation.getArgument(0);
            assertEquals(pessoaJuridicaRequestDTOPadrao.getRazaoSocial(), pessoaJuridica.getRazaoSocial());
            assertEquals(pessoaJuridicaRequestDTOPadrao.getCnpj(), pessoaJuridica.getCnpj());
            assertEquals(pessoaJuridicaRequestDTOPadrao.getEmail(), pessoaJuridica.getEmail());
            
            return pessoaJuridica;
        });

        pessoaJuridicaService.criar(pessoaJuridicaRequestDTOPadrao);
    }
    
    @Test
    void criarPessoaJuridicaDeveLancarExcecaoQuandoFalharAoSalvarPessoaJuridica() {
        when(pessoaJuridicaRepository.save(any(PessoaJuridica.class)))
            .thenThrow(new RuntimeException("Erro ao salvar pessoa jurídica"));

        assertThrows(RuntimeException.class, () -> {
            pessoaJuridicaService.criar(pessoaJuridicaRequestDTOPadrao);
        });
    }
    
    @Test
    void excluirPorIdDeveExcluirPessoaJuridicaQuandoExistir() {
        Long idPessoaJuridica = 94L;
        
        when(pessoaJuridicaRepository.existsById(idPessoaJuridica)).thenReturn(true);

        pessoaJuridicaService.excluirPorId(idPessoaJuridica);

        verify(pessoaJuridicaRepository, times(1)).deleteById(idPessoaJuridica);
        verify(pessoaJuridicaRepository, times(1)).flush();
    }
    
    @Test
    void excluirPorIdDeveLancarExcecaoQuandoPessoaJuridicaNaoExistir() {
    	Long idPessoaJuridica = 12742L;
    	
        when(pessoaJuridicaRepository.existsById(idPessoaJuridica)).thenReturn(false);

        assertThrows(
          PessoaJuridicaNaoEncontradaException.class, 
          () -> pessoaJuridicaService.excluirPorId(idPessoaJuridica)
        );
    }
    
    @Test
    void excluirPorIdDeveLancarEntidadeEmUsoExceptionQuandoDataIntegrityViolationExceptionOcorrer() {
        Long idPessoaJuridica = 12742L;
        
        when(pessoaJuridicaRepository.existsById(idPessoaJuridica)).thenReturn(true);
        
        doThrow(DataIntegrityViolationException.class).when(pessoaJuridicaRepository)
            .deleteById(idPessoaJuridica);

        assertThrows(
            EntidadeEmUsoException.class, 
            () -> pessoaJuridicaService.excluirPorId(idPessoaJuridica)
        );
    }
    
    private void assertPessoaJuridicaResponseDTOEqualsEntidade(
    		PessoaJuridicaResponseDTO dto, PessoaJuridica entidade) {
        assertEquals(dto.getId(), entidade.getId());
        assertEquals(dto.getRazaoSocial(), entidade.getRazaoSocial());
        assertEquals(dto.getCnpj(), entidade.getCnpj());
        assertEquals(dto.getEmail(), entidade.getEmail());
    }
    
    public PessoaJuridica criarPessoaJuridicaPadrao() {
        PessoaJuridica pessoaJuridica = new PessoaJuridica();
        pessoaJuridica.setId(1L);
        pessoaJuridica.setRazaoSocial("Tecnologia Avançada Ltda");
        pessoaJuridica.setCnpj("12345678000190");
        pessoaJuridica.setEmail("contato@techsolutions.com.br");
        
        return pessoaJuridica;
    }
    
	public PessoaJuridicaRequestDTO criarPessoaJuridicaRequestDTOPadrao() {
		PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO = new PessoaJuridicaRequestDTO();
		pessoaJuridicaRequestDTO.setRazaoSocial("Tecnologia Avançada Ltda");
		pessoaJuridicaRequestDTO.setCnpj("12345678000190");
		pessoaJuridicaRequestDTO.setEmail("contato@techsolutions.com.br");
		
		return pessoaJuridicaRequestDTO;
	}
    
}
