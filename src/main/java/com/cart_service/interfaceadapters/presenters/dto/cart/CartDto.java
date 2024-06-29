package com.cart_service.interfaceadapters.presenters.dto.cart;

import com.cart_service.interfaceadapters.presenters.dto.Dto;
import com.cart_service.util.enums.CartStatus;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDto extends Dto implements Serializable {

    private String costumerId;

    private List<ProductReservationDto> productReservationDto;

    private int productsQuantity;

    private double cartValue;

    private LocalDateTime creationDate;

    private LocalDateTime updateDate;

    private CartStatus status;

}
