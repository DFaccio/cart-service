package com.cart_service.frameworks.web;

import com.cart_service.interfaceadapters.controller.CartController;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/cart")
@Tag(name = "Carrinho", description = "Cria, atualiza e manipula carrinhos de compra")
public class CartWeb {

    @Resource
    private CartController cartController;

    @PostMapping(value = "/add-product/{costumertId}")
    public Mono<CartDto> add(@PathVariable String costumertId,
                             @Valid @RequestBody ReservationDto reservationDto){

        return cartController.add(costumertId, reservationDto);

    }

}
