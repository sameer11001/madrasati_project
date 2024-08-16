package com.webapp.madrasati.core.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration
@Profile("dev")
public class SwaggerConfig {
    // swagger
    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-apis")
                .pathsToMatch("/**")
                .build();
    }

    // swagger
    @Bean
    OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title("Madrasati API")
                .description("Madrasati API reference for developers")
                .version("v0.0.1")).addSecurityItem(new SecurityRequirement().addList("bearerAuth"))

                .components(new Components().addSecuritySchemes("bearerAuth",
                        new SecurityScheme().type(Type.HTTP).scheme("bearer").bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
    }
}
