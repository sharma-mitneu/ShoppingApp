package com.shopping_app.microservices.inventory_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI inventoryServiceOpenAPI(){
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service API")
                        .description("This API provides the functionality to check the availability of products in the inventory")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Inventory Service Wiki Documentation")
                        .url("https://inventory-service-dummy-url.com/docs"));
    }
}
