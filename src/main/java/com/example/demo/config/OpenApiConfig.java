package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("IntraHub API — Documentation")
                        .version("1.0.0")
                        .description("Documentação interativa das APIs REST do sistema IntraHub - Portal Corporativo e Intranet Integrada.")
                        .contact(new Contact()
                                .name("Kalicon Amorim")
                                .url("https://github.com/Kalicon/IntraHub")
                                .email("ti@intrahub.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}