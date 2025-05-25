package com.anton.kpo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class OpenApiConfig implements WebFluxConfigurer {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("KPO Gateway API")
                        .version("1.0")
                        .description("API Gateway for KPO Microservices")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
} 