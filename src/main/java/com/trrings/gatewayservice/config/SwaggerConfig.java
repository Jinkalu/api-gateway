package com.trrings.gatewayservice.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    private static final String SCHEME_NAME = "bearerAuth";
    private static final String SCHEME = "bearer";


    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(SCHEME_NAME, createSecurityScheme()))
                .addSecurityItem(new SecurityRequirement().addList(SCHEME_NAME))
                .info(getApiDocumentation());
    }

    private static Info getApiDocumentation() {
        return new Info()
                .title("API Documentation")
                .version("1.0")
                .description("API documentation using SpringDoc and Swagger UI");
    }


    private SecurityScheme createSecurityScheme() {
        return new SecurityScheme()
                .name(SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme(SCHEME);
    }
}
