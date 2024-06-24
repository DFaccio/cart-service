package com.cart_service.frameworks.db;

import com.cart_service.entities.Cart;
import com.cart_service.util.enums.CartStatus;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import java.util.Optional;

public interface CartRepository extends ReactiveMongoRepository<Cart, String> {

    @Query(value="{$and: [{'costumerId':{$eq:?0}},{'status':{$eq:?1}}]}")
    Optional<Cart> findByCostumerIdAndStatus(String costumerId, CartStatus status);

}
