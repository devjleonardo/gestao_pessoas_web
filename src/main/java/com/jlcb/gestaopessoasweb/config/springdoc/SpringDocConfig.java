package com.jlcb.gestaopessoasweb.config.springdoc;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.HashMap;
import java.util.Map;

import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jlcb.gestaopessoasweb.api.exception.ApiExceptionResponse;

import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;

@Configuration

public class SpringDocConfig {

    private static final String badRequestResponse = "BadRequestResponse";

    private static final String notFoundResponse = "NotFoundResponse";

    private static final String notAcceptableResponse = "NotAcceptableResponse";

    private static final String internalServerErrorResponse = "InternalServerErrorResponse";

    @Bean

    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Gestão de Pessoas Web API")
                .version("v1")
            ).externalDocs(new ExternalDocumentation()
                .description("Acessa a aplicação")
                .url("http://localhost:8080/gestao-pessoas-web/pessoas-fisicas.xhtml")
            ).components(new Components()
                .schemas(gerarSchemas())
                .responses(gerarResponses())
            );
    }

    @Bean

    public OpenApiCustomiser openApiCustomiser() {
        return openApi -> {
            openApi.getPaths()
            .values()
            .forEach(pathItem -> pathItem.readOperationsMap()
                .forEach((httpMethod, operation) -> {
                    ApiResponses responses = operation.getResponses();

                    switch (httpMethod) {
                        case GET:
                            responses.addApiResponse("404", new ApiResponse().description("Recurso não encontrado"));
                            responses.addApiResponse("406", new ApiResponse()
                                .description("Recurso não possui representação que poderia ser aceita pelo consumidor"));
                            responses.addApiResponse("500", new ApiResponse().description("Erro interno no servidor"));
                            break;
                        case POST:
                            responses.addApiResponse("400", new ApiResponse().description("Requisição inválida"));
                            responses.addApiResponse("500", new ApiResponse().description("Erro interno no servidor"));
                            break;
                        case PUT:
                            responses.addApiResponse("400", new ApiResponse().description("Requisição inválida"));
                            responses.addApiResponse("404", new ApiResponse().description("Recurso não encontrado"));
                            responses.addApiResponse("500", new ApiResponse().description("Erro interno no servidor"));
                            break;
                        case DELETE:
                            responses.addApiResponse("404", new ApiResponse().description("Recurso não encontrado"));
                            responses.addApiResponse("500", new ApiResponse().description("Erro interno no servidor"));
                            break;
                        default:
                            responses.addApiResponse("500", new ApiResponse().description("Erro interno no servidor"));
                            break;
                    }
                })
            );
        };
    }

    @SuppressWarnings("rawtypes")

    private Map<String, Schema> gerarSchemas() {
        final Map<String, Schema> schemaMap = new HashMap<>();

        Map<String, Schema> apiExceptionResponseSchema = 
        	ModelConverters.getInstance().read(ApiExceptionResponse.class);

        schemaMap.putAll(apiExceptionResponseSchema);

        return schemaMap;
    }

    private Map<String, ApiResponse> gerarResponses() {
        final Map<String, ApiResponse> apiResponseMap = new HashMap<>();

        Content content = new Content()
            .addMediaType(APPLICATION_JSON_VALUE,
                new MediaType().schema(new Schema<ApiExceptionResponse>().$ref("ApiExceptionResponse")));

        apiResponseMap.put(badRequestResponse, new ApiResponse()
            .description("Requisição inválida")
            .content(content));

        apiResponseMap.put(notFoundResponse, new ApiResponse()
            .description("Recurso não encontrado")
            .content(content));

        apiResponseMap.put(notAcceptableResponse, new ApiResponse()
            .description("Recurso não possui representação que poderia ser aceita pelo consumidor")
            .content(content));

        apiResponseMap.put(internalServerErrorResponse, new ApiResponse()
            .description("Erro interno no servidor")
            .content(content));

        return apiResponseMap;
    }

}