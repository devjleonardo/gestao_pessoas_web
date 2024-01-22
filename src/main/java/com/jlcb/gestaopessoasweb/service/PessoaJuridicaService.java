package com.jlcb.gestaopessoasweb.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlcb.gestaopessoasweb.api.dto.request.EnderecoRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.request.PessoaJuridicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.EnderecoResponseDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaJuridicaResponseDTO;
import com.jlcb.gestaopessoasweb.exception.EntidadeEmUsoException;
import com.jlcb.gestaopessoasweb.exception.PessoaJuridicaNaoEncontradaException;
import com.jlcb.gestaopessoasweb.model.Endereco;
import com.jlcb.gestaopessoasweb.model.PessoaJuridica;
import com.jlcb.gestaopessoasweb.repository.PessoaJuridicaRepository;

@Service
public class PessoaJuridicaService {
	
	private static final String MSG_PESSOA_JURIDICA_EM_USO 
	    = "Pessoa Jurídica de código %d não pode ser removida, pois está em uso";

	@Autowired
	private PessoaJuridicaRepository pessoaJuridicaRepository;
	
    public List<PessoaJuridicaResponseDTO> listar() {
        List<PessoaJuridica> todasPessoasJuridicas = pessoaJuridicaRepository.findAll();
        
        return todasPessoasJuridicas.stream()
	        .map(this::converterParaDTO)
	        .collect(Collectors.toList());
    }

	public PessoaJuridicaResponseDTO obterOuFalharPorId(Long id) {
		PessoaJuridica pessoaJuridica = pessoaJuridicaRepository.buscarComEnderecosPorId(id)
		    .orElseThrow(() -> new PessoaJuridicaNaoEncontradaException(id));

		PessoaJuridicaResponseDTO pessoaJuridicaResponseDTO = converterParaDTO(pessoaJuridica);
	    
	    return pessoaJuridicaResponseDTO;
	}
	
	@Transactional
    public PessoaJuridicaResponseDTO criar(PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO) {
		PessoaJuridica novaPessoaJuridica = converterParaEntidade(pessoaJuridicaRequestDTO);
        
        PessoaJuridica pessoaJuridicaSalva = pessoaJuridicaRepository.save(novaPessoaJuridica);
        
        PessoaJuridicaResponseDTO pessoaJuridicaResponseDTO = converterParaDTO(pessoaJuridicaSalva);
        
        return pessoaJuridicaResponseDTO;
    }
	
	@Transactional
	public PessoaJuridicaResponseDTO atualizar(Long id, PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO) {
		PessoaJuridica pessoaJuridicaExistente = pessoaJuridicaRepository.buscarComEnderecosPorId(id)
		    .orElseThrow(() -> new PessoaJuridicaNaoEncontradaException(id));

	    atualizarDadosPessoaJuridica(pessoaJuridicaExistente, pessoaJuridicaRequestDTO);

	    PessoaJuridica pessoaJuridicaAtualizada = pessoaJuridicaRepository.save(pessoaJuridicaExistente);

	    return converterParaDTO(pessoaJuridicaAtualizada);
	}
    
    @Transactional
    public void excluirPorId(Long id) {
        if (!pessoaJuridicaRepository.existsById(id)) {
            throw new PessoaJuridicaNaoEncontradaException(id);
        }
        
        try {
            pessoaJuridicaRepository.deleteById(id);
            pessoaJuridicaRepository.flush();
        } catch (DataIntegrityViolationException e) {
        	e.printStackTrace();
            throw new EntidadeEmUsoException(
                    String.format(MSG_PESSOA_JURIDICA_EM_USO, id));
        }
    }
    
    private void atualizarDadosPessoaJuridica(
    		PessoaJuridica pessoaJuridicaExistente, PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO) {
    	List<EnderecoRequestDTO> novosEnderecosDTO = pessoaJuridicaRequestDTO.getEnderecos();
    	
	 	pessoaJuridicaExistente.setRazaoSocial(pessoaJuridicaRequestDTO.getRazaoSocial());
	 	
		String cnpjSemMascara = pessoaJuridicaRequestDTO.getCnpj()
				.replaceAll("\\.", "").replaceAll("\\/", "").replaceAll("\\-", "");
		
	    pessoaJuridicaExistente.setCnpj(cnpjSemMascara);
	    pessoaJuridicaExistente.setEmail(pessoaJuridicaRequestDTO.getEmail());

        if (novosEnderecosDTO != null && !novosEnderecosDTO.isEmpty()) {
            for (Endereco enderecoExistente : pessoaJuridicaExistente.getEnderecos()) {
                for (EnderecoRequestDTO novoEnderecoDTO : novosEnderecosDTO) {
                    if (enderecoExistente.getId().equals(novoEnderecoDTO.getId())) {
                        atualizarEndereco(enderecoExistente, novoEnderecoDTO);
                        break;
                    }
                }
            }    
        }
    }
    
    private void atualizarEndereco(Endereco enderecoExistente, EnderecoRequestDTO novoEnderecoDTO) {
        enderecoExistente.setCep(novoEnderecoDTO.getCep());
        enderecoExistente.setLogradouro(novoEnderecoDTO.getLogradouro());
        enderecoExistente.setBairro(novoEnderecoDTO.getBairro());
        enderecoExistente.setNumero(novoEnderecoDTO.getNumero());
        enderecoExistente.setComplemento(novoEnderecoDTO.getComplemento());
        enderecoExistente.setCidade(novoEnderecoDTO.getCidade());
        enderecoExistente.setUf(novoEnderecoDTO.getUf());
    }
	
	private PessoaJuridicaResponseDTO converterParaDTO(PessoaJuridica pessoaJuridica) {
        PessoaJuridicaResponseDTO pessoaJuridicaResponseDTO = new PessoaJuridicaResponseDTO();
        pessoaJuridicaResponseDTO.setId(pessoaJuridica.getId());
        pessoaJuridicaResponseDTO.setRazaoSocial(pessoaJuridica.getRazaoSocial());
        pessoaJuridicaResponseDTO.setCnpj(pessoaJuridica.getCnpj());
        pessoaJuridicaResponseDTO.setEmail(pessoaJuridica.getEmail());

        pessoaJuridicaResponseDTO.setEnderecos(mapearEnderecos(pessoaJuridica.getEnderecos()));

        return pessoaJuridicaResponseDTO;
    }
    
    private List<EnderecoResponseDTO> mapearEnderecos(List<Endereco> enderecos) {
        return enderecos.stream()
            .map(this::converterParaEnderecoDTO)
            .collect(Collectors.toList());
    }
    
    private EnderecoResponseDTO converterParaEnderecoDTO(Endereco endereco) {
        EnderecoResponseDTO enderecoResponseDTO = new EnderecoResponseDTO();
        enderecoResponseDTO.setId(endereco.getId());
        enderecoResponseDTO.setCep(endereco.getCep());
        enderecoResponseDTO.setLogradouro(endereco.getLogradouro());
        enderecoResponseDTO.setBairro(endereco.getBairro());
        enderecoResponseDTO.setNumero(endereco.getNumero());
        enderecoResponseDTO.setComplemento(endereco.getComplemento());
        enderecoResponseDTO.setCidade(endereco.getCidade());
        enderecoResponseDTO.setUf(endereco.getUf());
        
        return enderecoResponseDTO;
    }
    
    private PessoaJuridica converterParaEntidade(PessoaJuridicaRequestDTO pessoaJuridicaRequestDTO) {
        PessoaJuridica entidade = new PessoaJuridica();
        entidade.setRazaoSocial(pessoaJuridicaRequestDTO.getRazaoSocial());
        
		String cnpjSemMascara = pessoaJuridicaRequestDTO.getCnpj()
				.replaceAll("\\.", "").replaceAll("\\/", "").replaceAll("\\-", "");
		
        entidade.setCnpj(cnpjSemMascara);
        entidade.setEmail(pessoaJuridicaRequestDTO.getEmail());

        if (pessoaJuridicaRequestDTO.getEnderecos() != null 
        		&& !pessoaJuridicaRequestDTO.getEnderecos().isEmpty()) {
            List<Endereco> enderecos 
                = converterListaEnderecos(pessoaJuridicaRequestDTO.getEnderecos(), entidade);
            
            for (Endereco endereco : enderecos) {
                entidade.adicionarEndereco(endereco);
            }
        }

        return entidade;
    }
    
    private List<Endereco> converterListaEnderecos(
    		List<EnderecoRequestDTO> enderecosRequestDTO, PessoaJuridica pessoaJuridica) {
    	if (enderecosRequestDTO != null) {
            return enderecosRequestDTO.stream()
                .map(enderecoRequestDTO -> converterParaEndereco(enderecoRequestDTO, pessoaJuridica))
                .collect(Collectors.toList());
    	} else {
            return Collections.emptyList();
        }
    }
    
    private Endereco converterParaEndereco(EnderecoRequestDTO enderecoRequestDTO, PessoaJuridica pessoaJuridica) {
        Endereco endereco = new Endereco();
        endereco.setCep(enderecoRequestDTO.getCep());
        endereco.setLogradouro(enderecoRequestDTO.getLogradouro());
        endereco.setBairro(enderecoRequestDTO.getBairro());
        endereco.setNumero(enderecoRequestDTO.getNumero());
        endereco.setComplemento(enderecoRequestDTO.getComplemento());
        endereco.setCidade(enderecoRequestDTO.getCidade());
        endereco.setUf(enderecoRequestDTO.getUf());
        endereco.setPessoa(pessoaJuridica);
        
        return endereco;
    }

}
