package com.saproject.centerSA.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI springTransactionAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Transaction API - CenterSA")
                        .description("API responsável pelas operações de saque, depósito e transferência")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Samuel Almeida de Sa")
                                .url("github.com/samuelsadev/CenterSA")
                                .email("samuelsa.dev@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentação completa no GitHub")
                        .url("https://github.com/samuelsadev/CenterSA"));
    }

    @Bean
    public OpenApiCustomizer customerGlobalHeaderOpenApiCustomizer() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {

            ApiResponses apiResponses = operation.getResponses();

            apiResponses.addApiResponse("200", createApiResponse("Operação realizada com sucesso!"));
            apiResponses.addApiResponse("201", createApiResponse("Recurso criado com sucesso!"));
            apiResponses.addApiResponse("204", createApiResponse("Recurso excluído com sucesso!"));
            apiResponses.addApiResponse("400", createApiResponse("Erro na requisição! Verifique os dados enviados."));
            apiResponses.addApiResponse("401", createApiResponse("Não autorizado! Token inválido ou ausente."));
            apiResponses.addApiResponse("403", createApiResponse("Acesso proibido! Permissões insuficientes."));
            apiResponses.addApiResponse("404", createApiResponse("Recurso não encontrado!"));
            apiResponses.addApiResponse("500", createApiResponse("Erro interno no servidor!"));
        }));
    }

    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);
    }
}
