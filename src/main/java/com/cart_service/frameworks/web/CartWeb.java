package com.cart_service.frameworks.web;

import com.cart_service.interfaceadapters.controller.CartController;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.util.enums.CartStatus;
import com.cart_service.util.exceptions.ValidationsException;
import com.cart_service.util.pagination.PagedResponse;
import com.cart_service.util.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/api/v1/cart")
@Tag(name = "Carrinho", description = "Cria, atualiza e manipula carrinho de compra")
public class CartWeb {

    @Resource
    private CartController cartController;

    @Operation(summary = "Adiciona um produto ao carrinho")
    @PostMapping(value = "/add-product/{costumertId}")
    public Mono<CartDto> addProductToCart(@PathVariable String costumertId,
                             @Valid @RequestBody ReservationListDto reservationDto) throws ValidationsException {

        return cartController.addProductToCart(costumertId, reservationDto);

    }

    @Operation(summary = "Atualiza um ou mais produtos de um carrinho")
    @PostMapping(value = "/updateCart")
    public Mono<CartDto> updateCart(@Valid @RequestBody CartDto cartDto) throws ValidationsException {

        return cartController.updateCart(cartDto);

    }

    @Operation(summary = "Consulta um carrinho")
    @GetMapping(value = "/findCart/{id}", consumes = "application/json", produces = "application/json")
    public Mono<CartDto> findCart(@PathVariable String id) throws ValidationsException {

        return cartController.getCart(id);

    }

    @Operation(summary = "Finaliza um carrinho")
    @PutMapping(value = "/confirm/{id}", consumes = "application/json", produces = "application/json")
    public Mono<CartDto> confirm(@PathVariable String id) throws ValidationsException {

        return cartController.confirm(id);

    }

    @Operation(summary = "Cancela um carrinho")
    @PutMapping(value = "/cancel/{id}", consumes = "application/json", produces = "application/json")
    public Mono<CartDto> cancel(@PathVariable String id) throws ValidationsException {

        return cartController.cancel(id);

    }

    @Operation(summary = "Busca todos os carrinhos, podendo filtrar por usuário e status")
    @GetMapping(value="/findCarts")
    public PagedResponse<CartDto> findCarts(
            @RequestParam("costumerId") String costumerId,
            @RequestParam("cartStatus") CartStatus cartStatus,
            @Parameter(description = "Default value 10. Max value 1000", example = "10") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage) {

        Pagination page = new Pagination(initialPage, pageSize);

        return cartController.findAll(costumerId, cartStatus, page);

    }

}
