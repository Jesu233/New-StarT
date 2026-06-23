package com.example.promocion.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API Microservicio de Promociones")
                        .version("1.0.0")
                        .description("""
                                Microservicio encargado de la gestión de promociones y descuentos.
                                
                                Características principales:
                                - Gestión de promociones (CRUD)
                                - Validación de reglas de descuento
                                - Registro de usos de promociones
                                - Cálculo de descuentos aplicables
                                """)
                        .contact(new Contact()
                                .name("StarTicket")
                                .email("starticket@contacto.cl")
                                .url("https://www.starticket.cl"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8088")
                                .description("Servidor de Desarrollo")
                ))
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .schemaRequirement(securitySchemeName,
                        new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")
                                .description("""
                                        Autenticación mediante JWT Token.
                                        El token debe ser enviado en el header Authorization: """));
    }
}