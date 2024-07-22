package com.cart_service.interfaceadapters.controller;

import com.cart_service.entities.Cart;
import com.cart_service.frameworks.external.inventory.ProductServiceInterface;
import com.cart_service.interfaceadapters.gateways.CartGateway;
import com.cart_service.interfaceadapters.helper.CartHelper;
import com.cart_service.interfaceadapters.presenters.CartPresenter;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.usercase.CartBusiness;
import com.cart_service.util.enums.CartStatus;
import com.cart_service.util.exceptions.ValidationsException;
import com.cart_service.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Component
public class CartController {

    @Resource
    private CartGateway cartGateway;

    @Resource
    private CartBusiness cartBusiness;

    @Resource
    private CartPresenter cartPresenter;

    @Resource
    private ProductServiceInterface productService;

    @Resource
    private CartHelper cartHelper;

    public Mono<CartDto> addProductToCart(String customerId, ReservationListDto reservationDto) throws ValidationsException, IOException {
        Mono<Cart> optional = cartGateway.findByCustomerIdAndStatus(customerId, CartStatus.CREATED);

        Cart cart = cartBusiness.addProductToCart(optional, customerId, reservationDto);

        cart = cartGateway.save(cart).block();

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));
    }

    public Mono<CartDto> findActiveCartByCustomerId(String customerId) throws ValidationsException {
        Mono<Cart> optional = cartGateway.findByCustomerIdAndStatus(customerId, CartStatus.CREATED);

        if (optional.blockOptional().isEmpty()) {
            throw new ValidationsException("0001");
        }

        Cart cart = optional.blockOptional().get();

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));
    }

    public Mono<CartDto> updateCart(CartDto cartDto) throws ValidationsException, IOException {
        Mono<Cart> optional = cartGateway.findById(cartDto.getId());

        Cart cart = cartBusiness.updateCart(cartDto, optional);

        cart = cartGateway.save(cart).block();

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));
    }

    public Mono<CartDto> confirm(String cartId) throws ValidationsException, IOException {
        Mono<Cart> optional = cartGateway.findById(cartId);

        Cart cart = cartBusiness.confirm(optional);

        List<String> reservationIds = cartBusiness.reservationIds(cart);

        ReservationListDto reservations = productService.confirmReservation(reservationIds);

        cartBusiness.updateReservationsCart(cart, reservations);

        cart = cartGateway.save(cart).block();

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));
    }

    public Mono<CartDto> cancel(String cartId) throws ValidationsException, IOException {
        Mono<Cart> optional = cartGateway.findById(cartId);

        Cart cart = cartBusiness.cancel(optional);

        List<String> reservationIds = cartBusiness.reservationIds(cart);

        ReservationListDto reservations = productService.cancelReservation(reservationIds);

        cartBusiness.updateReservationsCart(cart, reservations);

        cart = cartGateway.save(cart).block();

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));
    }

    public Mono<Page<CartDto>> findAllCarts(Pagination page, CartStatus cartStatus, String customerId) {
        Flux<Cart> cart;

        boolean customerIdFilter = customerId != null && !customerId.trim().isEmpty();
        boolean cartStatusFilter = cartStatus != null;

        if (cartStatusFilter && customerIdFilter) {
            cart = cartGateway.findAllByCustomerIdAndStatus(customerId, cartStatus);
        } else if (cartStatusFilter) {
            cart = cartGateway.findAllByStatus(cartStatus);
        } else if (customerIdFilter) {
            cart = cartGateway.findAllByCustomerId(customerId);
        } else {
            cart = cartGateway.findAll();
        }

        return cartHelper.convertFluxToMonoPage(cart, page);
    }

    public Mono<Page<CartDto>> findCustomerCartsFilter(String customerId, CartStatus cartStatus, Pagination page) {
        Flux<Cart> cart;

        if (cartStatus != null) {
            cart = cartGateway.findAllByCustomerIdAndStatus(customerId, cartStatus);
        } else {
            cart = cartGateway.findAllByCustomerId(customerId);
        }

        return cartHelper.convertFluxToMonoPage(cart, page);
    }
}
