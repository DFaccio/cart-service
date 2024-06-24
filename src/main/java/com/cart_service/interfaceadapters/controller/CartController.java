package com.cart_service.interfaceadapters.controller;

import com.cart_service.entities.Cart;
import com.cart_service.interfaceadapters.gateways.CartGateway;
import com.cart_service.interfaceadapters.presenters.CartPresenter;
import com.cart_service.interfaceadapters.presenters.dto.CartDto;
import com.cart_service.usercase.CartBusiness;
import com.cart_service.util.enums.CartStatus;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class CartController {

    @Resource
    private CartGateway cartGateway;

    @Resource
    private CartBusiness cartBusiness;

    @Resource
    private CartPresenter cartPresenter;

    public CartDto create(CartDto cartDto){

        Cart cart = cartPresenter.convert(cartDto);

        Optional<Cart> optional = cartGateway.findByCostumerIdAndStatus(cartDto.getCostumerId(), CartStatus.CREATED);

        if(optional.isPresent()){
            cart = cartBusiness.update(cart);
        }else{
            cart = cartBusiness.save(cart);
        }

        cart = cartGateway.save(cart);

        return cartPresenter.convert(cart);

    }

    public CartDto update(){


        return null;

    }

    public CartDto confirm(){


        return null;

    }

    public CartDto cancel(){


        return null;

    }

}
