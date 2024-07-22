package com.cart_service.interfaceadapters.gateways;

import com.cart_service.entities.Cart;
import com.cart_service.frameworks.db.CartRepository;
import com.cart_service.util.enums.CartStatus;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartGateway {

    @Resource
    private CartRepository cartRepository;

    public Mono<Cart> findByCustomerIdAndStatus(String customerId, CartStatus status) {
        return cartRepository.findByCustomerIdAndStatus(customerId, status);
    }

    public Mono<Cart> save(Cart cart) {
        return cartRepository.save(cart);
    }

    public Mono<Cart> findById(String id) {
        return cartRepository.findById(id);
    }

    public Flux<Cart> findAll() {
        return cartRepository.findAll();
    }

    public Flux<Cart> findAllByCustomerId(String customerId) {
        return cartRepository.findAllByCustomerId(customerId);
    }

    public Flux<Cart> findAllByStatus(CartStatus cartStatus) {
        return cartRepository.findAllByStatus(cartStatus);
    }

    public Flux<Cart> findAllByCustomerIdAndStatus(String customerId, CartStatus cartStatus) {
        return cartRepository.findAllByCustomerIdAndStatus(customerId, cartStatus);
    }
}
