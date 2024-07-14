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
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
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

//        if(optional.isPresent()){

//            cart = cartBusiness.update(optional.get(), reservationDto);
//
//            if(cartBusiness.skuAlreadyInCart(optional.get(), sku)) {
//            TODO chama método que verifica se o SKU já existe no carrinho
//             se existe, calcula a nova quantidade e faz o putReservation (/id/{id}/quantity/{quantity})
//             se não existe, chama o postReservation
//
//                reservationDto = productService.putReservation(dto).block();
//
//                productDto = getProduct(sku);
//
//
//            }else{
//
//                reservationDto = productService.createReservation(dto).block();
//
//                productDto = getProduct(sku);
//
//                cart = cartBusiness.update(optional.get(), reservationDto, productDto);
//
//            }
//        }else{

//            reservationDto = productService.createReservation(reservationDto);

//            productDto = getProduct(sku);

//            cart = cartBusiness.create(costumertId, reservationDto, productDto);

//        }

        cart = cartGateway.save(cart);

        Cart finalCart = cart;

        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));

    }

    public Mono<CartDto> getCart(String id){

        Cart cart = cartGateway.findById(id);

        return Mono.fromCallable(() ->cartPresenter.convert(cart));

    }

    public CartDto updateCart(){

//        TODO - chama business/helper para ver se houve remoção de quantidade,
//         monta reservationdto e chama serviço de reserva
//         /products/reservation/update enviar com quantidade 0 para cancelar reserva

        return null;

    }

    public Mono<CartDto> confirm(String cartId){

        List<String> reservationIds = new ArrayList<>();

        Cart cart = cartGateway.findById(cartId);

        cart = cartBusiness.confirm(cart);

        reservationIds = cartBusiness.reservationIds(cart);

        productService.confirmReservation(reservationIds);

        cart = cartGateway.save(cart);

        Cart finalCart = cart;

        return Mono.fromCallable(() ->cartPresenter.convert(finalCart));

    }

    public Mono<CartDto> cancel(String cartId){

        List<String> reservationIds = new ArrayList<>();

        Cart cart = cartGateway.findById(cartId);

        cart = cartBusiness.cancel(cart);

        reservationIds = cartBusiness.reservationIds(cart);

        productService.cancelReservation(reservationIds);

        cart = cartGateway.save(cart);

        Cart finalCart = cart;

        return Mono.fromCallable(() ->cartPresenter.convert(finalCart));

    }

}
