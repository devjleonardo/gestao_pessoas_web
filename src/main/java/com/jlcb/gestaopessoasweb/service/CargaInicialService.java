package com.jlcb.gestaopessoasweb.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jlcb.gestaopessoasweb.exception.ArquivoNaoEncontradoException;
import com.jlcb.gestaopessoasweb.model.CargaInicialEstado;
import com.jlcb.gestaopessoasweb.model.PessoaFisica;
import com.jlcb.gestaopessoasweb.repository.CargaInicialEstadoRepository;
import com.jlcb.gestaopessoasweb.repository.PessoaFisicaRepository;

@Service
public class CargaInicialService {

	private static final String CAMINHO_ARQUIVO = "/arquivos/pessoas.ods";

	@Autowired
	private CargaInicialEstadoRepository cargaInicialEstadoRepository;

	@Autowired
	private PessoaFisicaRepository pessoaFisicaRepository;

	public void realizarCargaInicial() {
		if (!isCargaInicialExecutada()) {
			try {
				
                Path arquivoPath = Paths.get(getClass().getResource(CAMINHO_ARQUIVO).toURI());
                
                if (!Files.exists(arquivoPath)) {
                    throw new ArquivoNaoEncontradoException("Arquivo não encontrado: " + CAMINHO_ARQUIVO);
                }
                
                InputStream inputStream = getClass().getResourceAsStream(CAMINHO_ARQUIVO);
				
				OdfSpreadsheetDocument documentoPlanilha = 
					OdfSpreadsheetDocument.loadDocument(inputStream);

				OdfTable primeiraPlanilha = documentoPlanilha.getTableList(true).get(0);

				int quantidadeLinhas = primeiraPlanilha.getRowCount();

				List<PessoaFisica> pessoasFisica = new ArrayList<>();

				for (int i = 1; i < quantidadeLinhas; i++) {
					Long id = Long.parseLong(primeiraPlanilha.getCellByPosition(0, i).getStringValue());
					String firstName = primeiraPlanilha.getCellByPosition(1, i).getStringValue();
					String lastName = primeiraPlanilha.getCellByPosition(2, i).getStringValue();
					String email = primeiraPlanilha.getCellByPosition(3, i).getStringValue();
					String cpf = primeiraPlanilha.getCellByPosition(4, i).getStringValue();

					String nomeCompleto = firstName + " " + lastName;

					PessoaFisica pessoaFisica = new PessoaFisica();
					pessoaFisica.setId(id);
					pessoaFisica.setNome(nomeCompleto);
					pessoaFisica.setCpf(cpf);
					pessoaFisica.setEmail(email);

					pessoasFisica.add(pessoaFisica);
				}

				pessoaFisicaRepository.saveAllAndFlush(pessoasFisica);
				
				setCargaInicialExecutada(true);
			} catch (ArquivoNaoEncontradoException e) {
				System.err.println("Erro: " + e.getMessage());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

    public boolean isCargaInicialExecutada() {
        return cargaInicialEstadoRepository.findById(1L)
            .map(CargaInicialEstado::isCargaInicialExecutada)
            .orElse(false);
    }
    
    private void setCargaInicialExecutada(boolean cargaInicialExecutada) {
        List<CargaInicialEstado> cargaInicialEstados = cargaInicialEstadoRepository.findAll();

        if (!cargaInicialEstados.isEmpty()) {
            CargaInicialEstado cargaInicialEstado = cargaInicialEstados.get(0);
            cargaInicialEstado.setCargaInicialExecutada(cargaInicialExecutada);
            
            cargaInicialEstadoRepository.save(cargaInicialEstado);
        } else {
            CargaInicialEstado novoCargaInicialEstado = new CargaInicialEstado();
            novoCargaInicialEstado.setCargaInicialExecutada(cargaInicialExecutada);
            
            cargaInicialEstadoRepository.save(novoCargaInicialEstado);
        }
    }

}
