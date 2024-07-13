package com.cart_service.interfaceadapters.gateways;

import com.cart_service.entities.Cart;
import com.cart_service.frameworks.db.CartRepository;
import com.cart_service.util.enums.CartStatus;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CartGateway {

    @Resource
    private CartRepository cartRepository;

    public Optional<Cart> findByCostumerIdAndStatus(String costumerId, CartStatus status){

        return cartRepository.findByCostumerIdAndStatus(costumerId, status);

    }

    public Cart save(Cart cart){

        return cartRepository.save(cart).block();

    }

    public Cart findById(String id){

        return cartRepository.findById(id).block();

    }

}
