package com.swyp.voiceshield.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI voiceShieldOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Voice Shield API")
                        .version("v1")
                        .description("Voice Shield backend API documentation"));
    }
}
