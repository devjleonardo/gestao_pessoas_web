package com.jlcb.gestaopessoasweb.api.beans.pessoajuridica;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jlcb.gestaopessoasweb.api.beans.util.HttpConfigUtil;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ManagedBean
@ViewScoped
public class PessoaJuridicaCrudBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String BASE_URL = HttpConfigUtil.BASE_URL + "pessoas-juridicas";

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

    private List<PessoaJuridicaDTO> pessoasJuridicas;

    private PessoaJuridicaDTO pessoaJuridicaSelecionada;
    
	private final OkHttpClient client;

	private final ObjectMapper objectMapper;
	
	private boolean edicao;
	
	public PessoaJuridicaCrudBean() {
		this.client = new OkHttpClient();
		this.objectMapper = new ObjectMapper();
	}

    @PostConstruct
    public void inicializar() {
    	carregarPessoasJuridicas();
    }

    public void novaPessoaJuridica() {
        this.pessoaJuridicaSelecionada = new PessoaJuridicaDTO();
    }

    public void salvarOuAtualizar() throws JsonProcessingException {
    	try {
    		if (pessoaJuridicaSelecionada.getId() == null) {
    			realizarRequisicaoCadastro(pessoaJuridicaSelecionada);
    		} else {
    			atualizarPessoaJuridica(pessoaJuridicaSelecionada);
    		}
    	} catch (Exception e) {
    		handleException(e);
		}
    }

    public void carregarPessoasJuridicas() {
    	try {
			Request request = new Request.Builder()
			    .url(BASE_URL)
			    .get()
			    .addHeader(HttpConfigUtil.HEADER_ACCEPT, HttpConfigUtil.APPLICATION_JSON)
			    .build();
			
			Response response = client.newCall(request).execute();
					
			String jsonResposta = response.body().string();

			Type tipoListaPessoaJuridicaDTO = 
			    new TypeToken<List<PessoaJuridicaDTO>>() { }.getType();
			
			pessoasJuridicas = new Gson().fromJson(jsonResposta, tipoListaPessoaJuridicaDTO);
    	} catch (Exception e) {
			handleException(e);
		}
    }

    public void atualizarPessoaJuridica(PessoaJuridicaDTO dto) throws JsonProcessingException {
    	try {
			String json = objectMapper.writeValueAsString(dto);
			RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);
			
			Request request = new Request.Builder()
					.url(BASE_URL + "/" + dto.getId())
					.put(body)
			        .addHeader(HttpConfigUtil.HEADER_ACCEPT, HttpConfigUtil.APPLICATION_JSON)
			        .addHeader(HttpConfigUtil.HEADER_CONTENT_TYPE, HttpConfigUtil.APPLICATION_JSON)
					.build();
			
			handleResponse(client.newCall(request).execute(), "Pessoa jurídica atualizada");
		} catch (Exception e) {
			handleException(e);
		}
    }
    
    public void excluir() {
		try {
			Request request = new Request.Builder()
			    .url(BASE_URL + "/" + pessoaJuridicaSelecionada.getId())
			    .delete()
			    .addHeader(HttpConfigUtil.HEADER_ACCEPT, HttpConfigUtil.APPLICATION_JSON)
				.build();

			handleResponse(client.newCall(request).execute(), "Pessoa jurídica removida");
		} catch (Exception e) {
			handleException(e);
		}
        pessoasJuridicas.remove(pessoaJuridicaSelecionada);
        pessoaJuridicaSelecionada = null;

        PrimeFaces.current().ajax().update("form:messages", "form:dt-pessoas-juridicas");
    }
    
    public String formatarCNPJ(String cnpj) {
        return cnpj.substring(0, 2) + "." +
               cnpj.substring(2, 5) + "." +
               cnpj.substring(5, 8) + "/" +
               cnpj.substring(8, 12) + "-" +
               cnpj.substring(12);
    }
    
    private void realizarRequisicaoCadastro(PessoaJuridicaDTO pessoaJuridicaDTO) throws Exception {
		try {
			String json = objectMapper.writeValueAsString(pessoaJuridicaDTO);
			RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);

			Request request = new Request.Builder()
			    .url(BASE_URL).post(body)
		        .addHeader(HttpConfigUtil.HEADER_ACCEPT, HttpConfigUtil.APPLICATION_JSON)
		        .addHeader(HttpConfigUtil.HEADER_CONTENT_TYPE, HttpConfigUtil.APPLICATION_JSON)
				.build();

			handleResponse(client.newCall(request).execute(), "Pessoa jurídica adicionada");
		} catch (Exception e) {
			handleException(e);
		}
    }
    
	private void handleResponse(Response response, String successMessage) throws IOException {
		if (response.isSuccessful()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(successMessage));
			PrimeFaces.current().executeScript("PF('cadastrarPessoaJuridicaDialog').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dt-pessoas-juridicas");
			carregarPessoasJuridicas();
		} else if (response.code() == 404) {
			handleNotFoundResponse(response);
		} else if (response.code() == 409) {
			handleConflictResponse(response);
		} else {
			System.err.println("Erro ao processar a resposta. Código de resposta: " + response.code());
		}
	}
    
	private void handleConflictResponse(Response response) throws IOException {
		handleErrorResponse(response, "Conflito");
	}

	private void handleNotFoundResponse(Response response) throws IOException {
		handleErrorResponse(response, "Recurso não encontrado");
	}

	private void handleErrorResponse(Response response, String defaultMessage) throws IOException {
		String responseBody = response.body().string();
		JsonNode jsonNode = objectMapper.readTree(responseBody);

		String errorMessage = jsonNode.has("message") ? jsonNode.get("message").asText() : defaultMessage;
		
	    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, null));
	    PrimeFaces.current().ajax().update("form:messages");
	}
	
	private void handleException(Exception e) {
		e.printStackTrace();
	}

    public List<PessoaJuridicaDTO> getPessoasJuridicas() {
        return pessoasJuridicas;
    }

    public void setPessoasJuridicas(List<PessoaJuridicaDTO> pessoasJuridicas) {
        this.pessoasJuridicas = pessoasJuridicas;
    }

    public PessoaJuridicaDTO getPessoaJuridicaSelecionada() {
        return pessoaJuridicaSelecionada;
    }

    public void setPessoaJuridicaSelecionada(PessoaJuridicaDTO pessoaJuridicaSelecionada) {
        this.pessoaJuridicaSelecionada = pessoaJuridicaSelecionada;
    }
    
    public boolean isEdicao() {
		return edicao;
	}
    
    public void setEdicao(boolean edicao) {
		this.edicao = edicao;
	}
    
}
