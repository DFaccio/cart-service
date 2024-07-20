package com.cart_service.frameworks.db;

import com.cart_service.entities.Cart;
import com.cart_service.util.enums.CartStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends ReactiveMongoRepository<Cart, String> {

    @Query(value="{$and: [{'costumerId':{$eq:?0}},{'cartStatus':{$eq:?1}}]}")
    Optional<Cart> findByCostumerIdAndStatus(String costumerId, CartStatus cartStatus);

    Page<Cart> findAll(Pageable pageable);

    @Query(value="{'costumerId':{$eq:?0}")
    Page<Cart> findAllByCostumerId(String costumerId, Pageable pageable);

    @Query(value="{'cartStatus':{$eq:?0}")
    Page<Cart> findAllByStatus(CartStatus cartStatus, Pageable pageable);

    @Query(value="{and: [{'costumerId':{$eq:?0}},{'cartStatus':{$eq:?1}}]}")
    Page<Cart> findAllByCostumerIdAndStatus(String costumerId, CartStatus cartStatus, Pageable pageable);

}
