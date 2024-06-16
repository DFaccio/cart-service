package com.cart_service.util.config.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiDocConfig() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Microsserviço para controle de carrinho de compras")
                                .description("APIs para criação e manipulação de carrinho de compras")
                                .version("1.0.0")
                ).addServersItem(new Server().url("http://localhost:7071/order-management-system/cart-microservice"))
                .addServersItem(new Server().url("http://localhost:7077"));
    }
}