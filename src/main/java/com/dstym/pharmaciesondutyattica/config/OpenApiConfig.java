package com.dstym.pharmaciesondutyattica.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Pharmacies On Duty Attica API")
                        .description("Getting the available pharmacies on duty in Attica, Greece.")
                        .version("1.0"));
    }
}
