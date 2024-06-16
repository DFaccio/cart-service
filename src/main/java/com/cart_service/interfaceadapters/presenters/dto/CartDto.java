package com.cart_service.interfaceadapters.presenters.dto;

import com.cart_service.util.enums.CartStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CartDto extends Dto implements Serializable {

    String cartId;

    String costumerId;

    List<ProductDetailsDto> productDetailsDto;

    int productsQuantity;

    double cartValue;

    LocalDateTime creationDate;

    LocalDateTime updateDate;

    CartStatus status;

}
