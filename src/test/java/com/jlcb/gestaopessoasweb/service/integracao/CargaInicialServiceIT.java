package com.jlcb.gestaopessoasweb.service.integracao;

import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import com.jlcb.gestaopessoasweb.repository.CargaInicialEstadoRepository;
import com.jlcb.gestaopessoasweb.repository.PessoaFisicaRepository;
import com.jlcb.gestaopessoasweb.service.CargaInicialService;

public class CargaInicialServiceIT {
	
    @InjectMocks
    private CargaInicialService cargaInicialService;
	
    @Mock
    private CargaInicialEstadoRepository cargaInicialEstadoRepository;

    @Mock
    private PessoaFisicaRepository pessoaFisicaRepository;

    @Before
    public void setUp() {
        // Configuração adicional, se necessário
    }

}
