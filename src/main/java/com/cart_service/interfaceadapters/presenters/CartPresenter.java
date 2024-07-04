package com.cart_service.interfaceadapters.presenters;

import com.cart_service.entities.Cart;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CartPresenter implements Presenter<Cart, CartDto>{

    @Resource
    private ProductReservationPresenter productReservationPresenter;

    @Override
    public CartDto convert(Cart document){

        CartDto cartDto = new CartDto();

        cartDto.setId(document.getId());
        cartDto.setCostumerId(document.getCostumerId());
        cartDto.setProductReservationDto(document.getProductReservation()
                .stream()
                .map(productReservation -> productReservationPresenter.convert(productReservation))
                .collect(Collectors.toList()));
        cartDto.setProductsQuantity(document.getProductsQuantity());
        cartDto.setCartValue(document.getCartValue());
        cartDto.setCreationDate(document.getCreationDate());
        cartDto.setUpdateDate(document.getUpdateDate());
        cartDto.setStatus(document.getStatus());

        return cartDto;

    }

    @Override
    public Cart convert(CartDto dto){

        Cart cart = new Cart();

        cart.setId(dto.getId());
        cart.setCostumerId(dto.getCostumerId());
        cart.setProductReservation(dto.getProductReservationDto()
                .stream()
                .map(productReservationDto -> productReservationPresenter.convert(productReservationDto))
                .collect(Collectors.toList()));
        cart.setProductsQuantity(dto.getProductsQuantity());
        cart.setCartValue(dto.getCartValue());
        cart.setCreationDate(dto.getCreationDate());
        cart.setUpdateDate(dto.getUpdateDate());
        cart.setStatus(dto.getStatus());

        return cart;

    }

}
