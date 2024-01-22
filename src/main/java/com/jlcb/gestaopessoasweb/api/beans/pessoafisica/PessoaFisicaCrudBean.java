package com.jlcb.gestaopessoasweb.api.beans.pessoafisica;

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
public class PessoaFisicaCrudBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final String BASE_URL = HttpConfigUtil.BASE_URL + "pessoas-fisicas";

	private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json");

	private List<PessoaFisicaDTO> pessoasFisicas;

	private PessoaFisicaDTO pessoaFisicaSelecionada;

	private final OkHttpClient client;

	private final ObjectMapper objectMapper;

	public PessoaFisicaCrudBean() {
		this.client = new OkHttpClient();
		this.objectMapper = new ObjectMapper();
	}

	@PostConstruct
	public void inicializar() {
		carregarPessoasFisicas();
	}

	public void novaPessoaFisica() {
		this.pessoaFisicaSelecionada = new PessoaFisicaDTO();
	}

	public void salvarOuAtualizar() {
		try {
			if (pessoaFisicaSelecionada.getId() == null) {
				realizarRequisicaoCadastro(pessoaFisicaSelecionada);
			} else {
				atualizarPessoaFisica(pessoaFisicaSelecionada);
			}
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void carregarPessoasFisicas() {
		try {
			Request request = new Request.Builder()
			    .url(BASE_URL)
			    .get()
			    .addHeader(HttpConfigUtil.HEADER_ACCEPT, HttpConfigUtil.APPLICATION_JSON)
			    .build();

			Response response = client.newCall(request).execute();
			
			String jsonResposta = response.body().string();

			Type tipoListaPessoaFisicaResponseDTO = 
			    new TypeToken<List<PessoaFisicaDTO>>() { }.getType();
			    
			pessoasFisicas = new Gson().fromJson(jsonResposta, tipoListaPessoaFisicaResponseDTO);
		} catch (IOException e) {
			handleException(e);
		}
	}

	public void atualizarPessoaFisica(PessoaFisicaDTO dto) {
		try {
			String json = objectMapper.writeValueAsString(dto);
			RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);

			Request request = new Request.Builder()
			    .url(BASE_URL + "/" + dto.getId())
			    .put(body)
		        .addHeader(HttpConfigUtil.HEADER_ACCEPT, HttpConfigUtil.APPLICATION_JSON)
		        .addHeader(HttpConfigUtil.HEADER_CONTENT_TYPE, HttpConfigUtil.APPLICATION_JSON)
				.build();

			handleResponse(client.newCall(request).execute(), "Pessoa física atualizada");
		} catch (Exception e) {
			handleException(e);
		}
	}

	public void excluir() {
		try {
			Request request = new Request.Builder()
				.url(BASE_URL + "/" + pessoaFisicaSelecionada.getId())
				.delete()
			    .addHeader(HttpConfigUtil.HEADER_ACCEPT, HttpConfigUtil.APPLICATION_JSON)
				.build();

			handleResponse(client.newCall(request).execute(), "Pessoa física removida");
		} catch (Exception e) {
			handleException(e);
		}

		pessoasFisicas.remove(pessoaFisicaSelecionada);
		pessoaFisicaSelecionada = null;

		PrimeFaces.current().ajax().update("form:messages", "form:dt-pessoas-fisicas");
	}
	
    public String formatarCPF(String cpf) {
    	return cpf.substring(0, 3) + "." +
               cpf.substring(3, 6) + "." +
               cpf.substring(6, 9) + "-" +
               cpf.substring(9);
    }

	private void realizarRequisicaoCadastro(PessoaFisicaDTO pessoaFisicaDTO) {
		try {
			String json = objectMapper.writeValueAsString(pessoaFisicaDTO);
			RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, json);

			Request request = new Request.Builder()
			    .url(BASE_URL)
			    .post(body)
		        .addHeader(HttpConfigUtil.HEADER_ACCEPT, HttpConfigUtil.APPLICATION_JSON)
		        .addHeader(HttpConfigUtil.HEADER_CONTENT_TYPE, HttpConfigUtil.APPLICATION_JSON)
				.build();

			handleResponse(client.newCall(request).execute(), "Pessoa física adicionada");
		} catch (Exception e) {
			handleException(e);
		}
	}

	private void handleResponse(Response response, String successMessage) throws IOException {
		if (response.isSuccessful()) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(successMessage));
			PrimeFaces.current().executeScript("PF('cadastrarPessoaFisicaDialog').hide()");
			PrimeFaces.current().ajax().update("form:messages", "form:dt-pessoas-fisicas");
			carregarPessoasFisicas();
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
	
	public List<PessoaFisicaDTO> getPessoasFisicas() {
		return pessoasFisicas;
	}

	public void setPessoasFisicas(List<PessoaFisicaDTO> pessoasFisicas) {
		this.pessoasFisicas = pessoasFisicas;
	}

	public PessoaFisicaDTO getPessoaFisicaSelecionada() {
		return pessoaFisicaSelecionada;
	}

	public void setPessoaFisicaSelecionada(PessoaFisicaDTO pessoaFisicaSelecionada) {
		this.pessoaFisicaSelecionada = pessoaFisicaSelecionada;
	}

}
