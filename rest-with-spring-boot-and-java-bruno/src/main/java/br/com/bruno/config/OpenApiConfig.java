package br.com.bruno.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("REST API com Java e Spring Boot")
						.version("v1")
						.description("Descrição sobre a API")
						.termsOfService("")
						.license(new License().name("Apache 2.0")
								.url("")));
	}
}
