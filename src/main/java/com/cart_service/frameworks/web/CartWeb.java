package com.cart_service.frameworks.web;

import com.cart_service.interfaceadapters.controller.CartController;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.util.exceptions.ValidationsException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/cart")
@Tag(name = "Carrinho", description = "Cria, atualiza e manipula carrinhos de compra")
public class CartWeb {

    @Resource
    private CartController cartController;

    @Operation(summary = "Adiciona um produto ao carrinho")
    @PostMapping(value = "/add-product/{costumertId}")
    public Mono<CartDto> addProductToCart(@PathVariable String costumertId,
                             @Valid @RequestBody ReservationListDto reservationDto) throws ValidationsException {

        return cartController.addProductToCart(costumertId, reservationDto);

    }

    @Operation(summary = "Consulta um carrinho")
    @GetMapping(value = "/{id}", consumes = "application/json")
    public Mono<CartDto> getCart(@PathVariable String id){

        return cartController.getCart(id);

    }
    @Operation(summary = "Finaliza um carrinho")
    @PutMapping(value = "/confirm/{id}", consumes = "application/json")
    public Mono<CartDto> confirm(@PathVariable String id){

        return cartController.confirm(id);

    }

    @Operation(summary = "Cancela um carrinho")
    @PutMapping(value = "/cancel/{id}", consumes = "application/json")
    public Mono<CartDto> cancel(@PathVariable String id){

        return cartController.cancel(id);

    }

}
