package com.jlcb.gestaopessoasweb.service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jlcb.gestaopessoasweb.api.dto.request.EnderecoRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.request.PessoaFisicaRequestDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.EnderecoResponseDTO;
import com.jlcb.gestaopessoasweb.api.dto.response.PessoaFisicaResponseDTO;
import com.jlcb.gestaopessoasweb.exception.EntidadeEmUsoException;
import com.jlcb.gestaopessoasweb.exception.PessoaJuridicaNaoEncontradaException;
import com.jlcb.gestaopessoasweb.model.Endereco;
import com.jlcb.gestaopessoasweb.model.PessoaFisica;
import com.jlcb.gestaopessoasweb.repository.PessoaFisicaRepository;

@Service
public class PessoaFisicaService {
	
	private static final String MSG_PESSOA_FISICA_EM_USO 
	    = "Pessoa Física de código %d não pode ser removida, pois está em uso";

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;
	
    public List<PessoaFisicaResponseDTO> listar() {
        List<PessoaFisica> todasPessoasFisicas = pessoaFisicaRepository.findAll();
        
        return todasPessoasFisicas.stream()
	        .map(this::converterParaDTO)
	        .collect(Collectors.toList());
    }

	public PessoaFisicaResponseDTO obterOuFalharPorId(Long idPessoaFisica) {
		PessoaFisica pessoaFisica = pessoaFisicaRepository.buscarComEnderecosPorId(idPessoaFisica)
		    .orElseThrow(() -> new PessoaJuridicaNaoEncontradaException(idPessoaFisica));

		PessoaFisicaResponseDTO pessoaFisicaResponseDTO = converterParaDTO(pessoaFisica);
	    
	    return pessoaFisicaResponseDTO;
	}
	
	@Transactional
    public PessoaFisicaResponseDTO criar(PessoaFisicaRequestDTO pessoaFisicaRequestDTO) {
        PessoaFisica novaPessoaFisica = converterParaEntidade(pessoaFisicaRequestDTO);
        
        PessoaFisica pessoaFisicaSalva = pessoaFisicaRepository.save(novaPessoaFisica);
        
        PessoaFisicaResponseDTO pessoaFisicaResponseDTO = converterParaDTO(pessoaFisicaSalva);
        
        return pessoaFisicaResponseDTO;
    }
	
	@Transactional
	public PessoaFisicaResponseDTO atualizar(Long id, PessoaFisicaRequestDTO pessoaFisicaRequestDTO) {
		PessoaFisica pessoaFisicaExistente = pessoaFisicaRepository.buscarComEnderecosPorId(id)
		    .orElseThrow(() -> new PessoaJuridicaNaoEncontradaException(id));

	    atualizarDadosPessoaFisica(pessoaFisicaExistente, pessoaFisicaRequestDTO);

	    PessoaFisica pessoaFisicaAtualizada = pessoaFisicaRepository.save(pessoaFisicaExistente);

	    return converterParaDTO(pessoaFisicaAtualizada);
	}
    
    @Transactional
    public void excluirPorId(Long idPessoaFisica) {
        if (!pessoaFisicaRepository.existsById(idPessoaFisica)) {
            throw new PessoaJuridicaNaoEncontradaException(idPessoaFisica);
        }
        
        try {
            pessoaFisicaRepository.deleteById(idPessoaFisica);
            pessoaFisicaRepository.flush();
        } catch (DataIntegrityViolationException e) {
        	e.printStackTrace();
            throw new EntidadeEmUsoException(
                    String.format(MSG_PESSOA_FISICA_EM_USO, idPessoaFisica));
        }
    }
    
    private void atualizarDadosPessoaFisica(
    		PessoaFisica pessoaFisicaExistente, PessoaFisicaRequestDTO pessoaFisicaRequestDTO) {
    	List<EnderecoRequestDTO> novosEnderecosDTO = pessoaFisicaRequestDTO.getEnderecos();
    	
	 	pessoaFisicaExistente.setNome(pessoaFisicaRequestDTO.getNome());
	 	
        String cpfSemMascara = pessoaFisicaRequestDTO.getCpf().replaceAll("\\.", "").replaceAll("\\-", "");
	    pessoaFisicaExistente.setCpf(cpfSemMascara);
	    
	    pessoaFisicaExistente.setEmail(pessoaFisicaRequestDTO.getEmail());

        if (novosEnderecosDTO != null && !novosEnderecosDTO.isEmpty()) {
            for (Endereco enderecoExistente : pessoaFisicaExistente.getEnderecos()) {
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
	
	private PessoaFisicaResponseDTO converterParaDTO(PessoaFisica pessoaFisica) {
        PessoaFisicaResponseDTO pessoaFisicaResponseDTO = new PessoaFisicaResponseDTO();
        pessoaFisicaResponseDTO.setId(pessoaFisica.getId());
        pessoaFisicaResponseDTO.setNome(pessoaFisica.getNome());
        pessoaFisicaResponseDTO.setCpf(pessoaFisica.getCpf());
        pessoaFisicaResponseDTO.setEmail(pessoaFisica.getEmail());

        pessoaFisicaResponseDTO.setEnderecos(mapearEnderecos(pessoaFisica.getEnderecos()));

        return pessoaFisicaResponseDTO;
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
    
    private PessoaFisica converterParaEntidade(PessoaFisicaRequestDTO pessoaFisicaRequestDTO) {
        PessoaFisica entidade = new PessoaFisica();
        entidade.setNome(pessoaFisicaRequestDTO.getNome());
        
        String cpfSemMascara = pessoaFisicaRequestDTO.getCpf().replaceAll("\\.", "").replaceAll("\\-", "");
        entidade.setCpf(cpfSemMascara);
        
        entidade.setEmail(pessoaFisicaRequestDTO.getEmail());

        if (pessoaFisicaRequestDTO.getEnderecos() != null 
        		&& !pessoaFisicaRequestDTO.getEnderecos().isEmpty()) {
            List<Endereco> enderecos 
                = converterListaEnderecos(pessoaFisicaRequestDTO.getEnderecos(), entidade);
            
            for (Endereco endereco : enderecos) {
                entidade.adicionarEndereco(endereco);
            }
        }

        return entidade;
    }
    
    private List<Endereco> converterListaEnderecos(
    		List<EnderecoRequestDTO> enderecosRequestDTO, PessoaFisica pessoaFisica) {
    	if (enderecosRequestDTO != null) {
            return enderecosRequestDTO.stream()
                .map(enderecoRequestDTO -> converterParaEndereco(enderecoRequestDTO, pessoaFisica))
                .collect(Collectors.toList());
    	} else {
            return Collections.emptyList();
        }
    }
    
    private Endereco converterParaEndereco(EnderecoRequestDTO enderecoRequestDTO, PessoaFisica pessoaFisica) {
        Endereco endereco = new Endereco();
        endereco.setCep(enderecoRequestDTO.getCep());
        endereco.setLogradouro(enderecoRequestDTO.getLogradouro());
        endereco.setBairro(enderecoRequestDTO.getBairro());
        endereco.setNumero(enderecoRequestDTO.getNumero());
        endereco.setComplemento(enderecoRequestDTO.getComplemento());
        endereco.setCidade(enderecoRequestDTO.getCidade());
        endereco.setUf(enderecoRequestDTO.getUf());
        endereco.setPessoa(pessoaFisica);
        
        return endereco;
    }

}
