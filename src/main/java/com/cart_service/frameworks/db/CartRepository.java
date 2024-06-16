package com.cart_service.frameworks.db;

import com.cart_service.entities.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CartRepository extends ReactiveMongoRepository<Cart, String> {



}
