package com.cart_service.frameworks.db;

import com.cart_service.entities.Cart;
import com.cart_service.util.enums.CartStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.Optional;

@Repository
public interface CartRepository extends ReactiveMongoRepository<Cart, String> {

    @Query(value="{$and: [{'customerId':{$eq:?0}},{'cartStatus':{$eq:?1}}]}")
    Optional<Cart> findByCustomerIdAndStatus(String customerId, CartStatus cartStatus);

    @Query(value="{'customerId':{$eq:?0}")
    Flux<Cart> findAllByCustomerId(String customerId);

    @Query(value="{'cartStatus':{$eq:?0}")
    Flux<Cart> findAllByStatus(CartStatus cartStatus);

    @Query(value="{and: [{'customerId':{$eq:?0}},{'cartStatus':{$eq:?1}}]}")
    Flux<Cart> findAllByCustomerIdAndStatus(String customerId, CartStatus cartStatus);

}
