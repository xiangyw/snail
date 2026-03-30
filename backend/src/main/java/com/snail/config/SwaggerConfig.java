package com.snail.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Snail商城API")
                        .version("1.0.0")
                        .description("Snail项目商城模块API文档")
                        .contact(new Contact()
                                .name("Snail开发团队")
                                .email("dev@snail.com")
                                .url("https://snail.example.com")));
    }
}