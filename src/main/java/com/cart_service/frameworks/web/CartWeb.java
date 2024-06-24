package com.cart_service.frameworks.web;

import com.cart_service.interfaceadapters.controller.CartController;
import com.cart_service.interfaceadapters.presenters.dto.CartDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/cart")
@Tag(name = "Carrinho", description = "Cria, atualiza e manipula carrinhos de compra")
public class CartWeb {

    @Resource
    private CartController cartController;

    @PostMapping(value = "/create")
    public Mono<CartDto> create(@Valid @RequestBody CartDto cartDto){

        return Mono.fromCallable(() -> cartController.create(cartDto));

    }

}
