package com.cart_service.frameworks.web;

import com.cart_service.interfaceadapters.controller.CartController;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.util.enums.CartStatus;
import com.cart_service.util.exceptions.ValidationsException;
import com.cart_service.util.pagination.Pagination;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/v1/cart")
@Tag(name = "Carrinho", description = "Cria, atualiza e manipula carrinho de compra")
public class CartWeb {

    @Resource
    private CartController cartController;

    @Operation(summary = "Adiciona um produto ao carrinho")
    @PostMapping(value = "/add-product")
    public Mono<CartDto> addProductToCart(@Parameter(description = "ID do usuário", example = "123456")
                                          @RequestParam("customerId") String customerId,
                                          @Valid @RequestBody ReservationListDto reservationDto) throws ValidationsException, IOException {
        return cartController.addProductToCart(customerId, reservationDto);
    }

    @Operation(summary = "Atualiza um ou mais produtos de um carrinho")
    @PostMapping(value = "/update-cart")
    public Mono<CartDto> updateCart(@Valid @RequestBody CartDto cartDto) throws ValidationsException, IOException {
        return cartController.updateCart(cartDto);
    }

    @Operation(summary = "Consulta um carrinho")
    @GetMapping(value = "/find-active-customer-cart", produces = "application/json")
    public Mono<CartDto> findActiveCartByCustomerId(@Parameter(description = "ID do usuário", example = "123456")
                                                    @RequestParam("customerId") String customerId) throws ValidationsException {

        return cartController.findActiveCartByCustomerId(customerId);

    }

    @Operation(summary = "Finaliza um carrinho")
    @PutMapping(value = "/confirm-cart", produces = "application/json")
    public Mono<CartDto> confirm(@Parameter(description = "ID do carrinho", example = "123456")
                                 @RequestParam("cartId") String cartId) throws ValidationsException, IOException {
        return cartController.confirm(cartId);
    }

    @Operation(summary = "Cancela um carrinho")
    @PutMapping(value = "/cancel-cart", produces = "application/json")
    public Mono<CartDto> cancel(@Parameter(description = "ID do carrinho", example = "123456")
                                @RequestParam("cartId") String cartId) throws ValidationsException, IOException {
        return cartController.cancel(cartId);
    }

    @Operation(summary = "Busca todos os carrinhos")
    @GetMapping(value = "/admin/find-carts", produces = "application/json")
    public Mono<Page<CartDto>> findAllCarts(
            @Parameter(description = "ID do usuário", example = "123456")
            @RequestParam(required = false) String customerId,
            @Parameter(description = "Status do carrinho", example = "CREATED")
            @RequestParam(required = false) CartStatus cartStatus,
            @Parameter(description = "Default value 10. Max value 1000", example = "10") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage) {
        Pagination page = new Pagination(initialPage, pageSize);

        return cartController.findAllCarts(page, cartStatus, customerId);
    }

    @Operation(summary = "Busca carrinhos, podendo filtrar por usuário e status")
    @GetMapping(value = "/find-carts", produces = "application/json")
    public Mono<Page<CartDto>> findCustomerCartsFilter(
            @Parameter(description = "ID do usuário", example = "123456")
            @RequestParam(required = true) String customerId,
            @Parameter(description = "Status do carrinho", example = "CREATED")
            @RequestParam(required = false) CartStatus cartStatus,
            @Parameter(description = "Default value 10. Max value 1000", example = "10") @RequestParam(required = false) Integer pageSize,
            @Parameter(description = "Default value 0", example = "0") @RequestParam(required = false) Integer initialPage) {

        Pagination page = new Pagination(initialPage, pageSize);

        return cartController.findCustomerCartsFilter(customerId, cartStatus, page);
    }
}
