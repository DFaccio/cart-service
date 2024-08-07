package com.cart_service.entities;

import com.cart_service.util.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetails implements Serializable {

    private String sku;

    private String name;

    private double price;

    private int quantity;

    private double total;

    private ReservationStatus status;

}
