package com.cart_service.interfaceadapters.gateways;

import com.cart_service.entities.Cart;
import com.cart_service.frameworks.db.CartRepository;
import com.cart_service.util.enums.CartStatus;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Cart> findAll(Pageable pageable){

        return cartRepository.findAll(pageable);

    }

    public Page<Cart> findAllByCostumerId(String costumerId, Pageable pageable){

        return cartRepository.findAllByCostumerId(costumerId, pageable);

    }

    public Page<Cart> findAllByStatus(CartStatus cartStatus, Pageable pageable){

        return cartRepository.findAllByStatus(cartStatus, pageable);

    }

    public Page<Cart> findAllByCostumerIdAndStatus(String costumerId, CartStatus cartStatus, Pageable pageable){

        return cartRepository.findAllByCostumerIdAndStatus(costumerId, cartStatus, pageable);

    }

}
