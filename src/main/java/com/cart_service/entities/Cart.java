package com.cart_service.entities;

import com.cart_service.util.enums.CartStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Document("cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart implements Serializable {

    @Id
    private String Id;

    private String customerId;

    private int productsQuantity;

    private double cartValue;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private CartStatus cartStatus;

    private List<ProductReservation> productReservation;

}
