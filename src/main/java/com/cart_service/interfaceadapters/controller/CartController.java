package com.cart_service.interfaceadapters.controller;

import com.cart_service.entities.Cart;
import com.cart_service.interfaceadapters.gateways.CartGateway;
import com.cart_service.interfaceadapters.presenters.CartPresenter;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.service.ProductService;
import com.cart_service.usercase.CartBusiness;
import com.cart_service.util.enums.CartStatus;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

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

    public Mono<CartDto> add(String costumertId, ReservationDto dto){

        ProductDto productDto;

        String sku = dto.getSku();
        int quantity = dto.getQuantity();

        ReservationDto reservationDto;

        reservationDto = productService.postReservation(dto).block();

        productDto = productService.getProduct(sku).block();

        Cart cart;

        Optional<Cart> optional = cartGateway.findByCostumerIdAndStatus(costumertId, CartStatus.CREATED);

        if(optional.isPresent()){

            cart = cartBusiness.update(optional.get(), reservationDto, productDto);

        }else{

            cart = cartBusiness.save(costumertId, reservationDto, productDto);

        }

        cart = cartGateway.save(cart);

        Cart finalCart = cart;
        return Mono.fromCallable(() -> cartPresenter.convert(finalCart));

    }

    public CartDto update(){

//        TODO - chama business/helper para ver se houve remoção de quantidade,
//         monta reservationdto e chama serviço de reserva
//         /products/reservation/update enviar com quantidade 0 para cancelar reserva

        return null;

    }

    public CartDto confirm(){


        return null;

    }

    public CartDto cancel(){


        return null;

    }

}
