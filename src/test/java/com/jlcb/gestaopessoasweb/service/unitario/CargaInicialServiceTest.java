package com.jlcb.gestaopessoasweb.service.unitario;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.odftoolkit.odfdom.doc.OdfSpreadsheetDocument;
import org.odftoolkit.odfdom.doc.table.OdfTable;
import org.odftoolkit.odfdom.doc.table.OdfTableCell;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.jlcb.gestaopessoasweb.model.CargaInicialEstado;
import com.jlcb.gestaopessoasweb.repository.CargaInicialEstadoRepository;
import com.jlcb.gestaopessoasweb.repository.PessoaFisicaRepository;
import com.jlcb.gestaopessoasweb.service.CargaInicialService;

@SpringBootTest
public class CargaInicialServiceTest {

	@Mock
	private PessoaFisicaRepository pessoaFisicaRepository;

	@Mock
	private CargaInicialEstadoRepository cargaInicialEstadoRepository;

	@MockBean
	private OdfSpreadsheetDocument documentoPlanilha;

	@MockBean
	private OdfTable primeiraPlanilha;

	@MockBean
	private InputStream inputStream;

	@InjectMocks
	private CargaInicialService cargaInicialService;

	@Test
	public void aoCarregarDadosDaPlanilhaODSDeveSalvarPessoasFisicasEAtualizarEstadoDeCarga() {
		OdfSpreadsheetDocument documentoPlanilhaMock = mock(OdfSpreadsheetDocument.class);

		OdfTable primeiraPlanilhaMock = mock(OdfTable.class);

		when(documentoPlanilhaMock.getTableList(true)).thenReturn(Arrays.asList(primeiraPlanilhaMock));

		when(primeiraPlanilhaMock.getRowCount()).thenReturn(3);

		when(primeiraPlanilhaMock.getCellByPosition(0, 1)).thenReturn(mock(OdfTableCell.class));
		when(primeiraPlanilhaMock.getCellByPosition(1, 1)).thenReturn(mock(OdfTableCell.class));
		when(primeiraPlanilhaMock.getCellByPosition(2, 1)).thenReturn(mock(OdfTableCell.class));
		when(primeiraPlanilhaMock.getCellByPosition(3, 1)).thenReturn(mock(OdfTableCell.class));
		when(primeiraPlanilhaMock.getCellByPosition(4, 1)).thenReturn(mock(OdfTableCell.class));

		when(primeiraPlanilhaMock.getCellByPosition(0, 1).getStringValue()).thenReturn("1");
		when(primeiraPlanilhaMock.getCellByPosition(1, 1).getStringValue()).thenReturn("Agatha");
		when(primeiraPlanilhaMock.getCellByPosition(2, 1).getStringValue()).thenReturn("Andrea");
		when(primeiraPlanilhaMock.getCellByPosition(3, 1).getStringValue()).thenReturn("agathamota83@google.com");
		when(primeiraPlanilhaMock.getCellByPosition(4, 1).getStringValue()).thenReturn("22113792060");

		cargaInicialService.realizarCargaInicial();

		verify(pessoaFisicaRepository, times(1)).saveAllAndFlush(anyList());
		verify(cargaInicialEstadoRepository, times(1)).save(any(CargaInicialEstado.class));
	}
	
	@Test
	public void carregarDadosDoArquivoODSCargaNaoExecutadaDeveSalvarDados() {
		when(cargaInicialEstadoRepository.findById(1L)).thenReturn(Optional.of(new CargaInicialEstado(false)));

		cargaInicialService.realizarCargaInicial();

		verify(pessoaFisicaRepository, times(1)).saveAllAndFlush(anyList());
		verify(cargaInicialEstadoRepository, times(1)).save(any(CargaInicialEstado.class));
	}

	@Test
	public void carregarDadosDoArquivoODSCargaJaExecutadaNaoDeveSalvarDados() {
		when(cargaInicialEstadoRepository.findById(1L)).thenReturn(Optional.of(new CargaInicialEstado(true)));

		cargaInicialService.realizarCargaInicial();

		verify(pessoaFisicaRepository, never()).saveAllAndFlush(anyList());
		verify(cargaInicialEstadoRepository, never()).save(any(CargaInicialEstado.class));
	}
	
}
