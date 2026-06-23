package com.example.notificaciones.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(){

        final String securitySchenemeName = "bearerAuth";

        return new OpenAPI()
                .info(new Info()
                        .title("API Notificaciones")
                        .version("1.0")
                        .description("Microservicio de Pacientes")
                        .contact(new Contact()
                                .name("StarTicket")
                                .email("starticket@contacto.cl")))
                .addSecurityItem(
                        new SecurityRequirement()
                                .addList(securitySchenemeName))
                .schemaRequirement(securitySchenemeName,
                        new SecurityScheme()
                                .name(securitySchenemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"));
    }
}
