package com.cart_service.interfaceadapters.presenters;

import com.cart_service.entities.Cart;
import com.cart_service.interfaceadapters.presenters.dto.CartDto;
import jakarta.annotation.Resource;
import org.springframework.security.oauth2.client.endpoint.WebClientReactiveAuthorizationCodeTokenResponseClient;

import java.util.stream.Collectors;

public class CartPresenter implements Presenter<Cart, CartDto>{

    @Resource
    private ProductDetailsPresenter productDetailsPresenter;

    @Override
    public CartDto convert(Cart document){

        CartDto cartDto = new CartDto();

        cartDto.setCartId(document.getCartId());
        cartDto.setCostumerId(document.getCostumerId());
        cartDto.setProductDetailsDto(document.getProductDetails()
                .stream()
                .map(productDetails -> productDetailsPresenter.convert(productDetails))
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

        cart.setCartId(dto.getCartId());
        cart.setCostumerId(dto.getCostumerId());
        cart.setProductDetails(dto.getProductDetailsDto()
                .stream()
                .map(productDetails -> productDetailsPresenter.convert(productDetails))
                .collect(Collectors.toList()));
        cart.setProductsQuantity(dto.getProductsQuantity());
        cart.setCartValue(dto.getCartValue());
        cart.setCreationDate(dto.getCreationDate());
        cart.setUpdateDate(dto.getUpdateDate());
        cart.setStatus(dto.getStatus());

        return cart;

    }

}
