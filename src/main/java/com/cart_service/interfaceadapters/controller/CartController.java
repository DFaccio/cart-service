package com.cart_service.interfaceadapters.controller;

import com.cart_service.entities.Cart;
import com.cart_service.interfaceadapters.gateways.CartGateway;
import com.cart_service.interfaceadapters.presenters.CartPresenter;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.service.ProductService;
import com.cart_service.usercase.CartBusiness;
import com.cart_service.util.enums.CartStatus;
import com.cart_service.util.exceptions.ValidationsException;
import com.cart_service.util.pagination.PagedResponse;
import com.cart_service.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Component
public class CartController {

    @Resource
    private CartGateway cartGateway;

    @Resource
    private CartBusiness cartBusiness;

    @Resource
    private CartPresenter cartPresenter;

    @Resource
    private ProductService productService;

    public Mono<CartDto> addProductToCart(String costumertId, ReservationListDto reservationDto) throws ValidationsException {

        Optional<Cart> optional = cartGateway.findByCostumerIdAndStatus(costumertId, CartStatus.CREATED);

        Cart cart = cartBusiness.addProductToCart(optional, costumertId, reservationDto);

        cart = cartGateway.save(cart);

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));

    }

    public Mono<CartDto> getCart(String id) throws ValidationsException {

        Optional<Cart> optional = cartGateway.findById(id);

        Cart cart;

        if(optional.isPresent()){

            cart = optional.get();

        }else{

            throw new ValidationsException("0001");

        }

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));

    }

    public Mono<CartDto> updateCart(CartDto cartDto) throws ValidationsException {

        Optional<Cart> optional = cartGateway.findById(cartDto.getId());

        Cart cart;

        cart = cartBusiness.updateCart(cartDto, optional);

        cart = cartGateway.save(cart);

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));

    }

    public Mono<CartDto> confirm(String cartId) throws ValidationsException {

        List<String> reservationIds;

        Optional<Cart> optional = cartGateway.findById(cartId);

        Cart cart;

        cart = cartBusiness.confirm(optional);

        reservationIds = cartBusiness.reservationIds(cart);

        productService.confirmReservation(reservationIds);

        cart = cartGateway.save(cart);

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));

    }

    public Mono<CartDto> cancel(String cartId) throws ValidationsException {

        List<String> reservationIds;

        Optional<Cart> optional = cartGateway.findById(cartId);

        Cart cart;

        cart = cartBusiness.cancel(optional);

        reservationIds = cartBusiness.reservationIds(cart);

        productService.cancelReservation(reservationIds);

        cart = cartGateway.save(cart);

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));

    }

    public PagedResponse<CartDto> findAll(String costumerId, CartStatus cartStatus, Pagination page){

        Pageable pageable = PageRequest.of(page.getPage(), page.getPageSize());

        Page<Cart> cart = null;

        boolean costumerIdFilter = costumerId != null && !costumerId.trim().isEmpty();
        boolean cartStatusFilter = cartStatus != null && !String.valueOf(cartStatus).trim().isEmpty();

        if (!costumerIdFilter && !cartStatusFilter) {
            cart = cartGateway.findAll(pageable);
        } else if (costumerIdFilter && !cartStatusFilter) {
            cart = cartGateway.findAllByCostumerId(costumerId, pageable);
        }else if (!costumerIdFilter) {
            cart = cartGateway.findAllByStatus(cartStatus, pageable);
        }else {
            cart = cartGateway.findAllByCostumerIdAndStatus(costumerId, cartStatus, pageable);
        }

        return cartPresenter.convertDocuments(cart);

    }
}
