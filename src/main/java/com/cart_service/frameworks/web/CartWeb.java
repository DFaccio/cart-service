package com.cart_service.frameworks.web;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cart")
@Tag(name = "Carrinho", description = "Cria, atualiza e manipula carrinhos de compra")
public class CartWeb {



}
