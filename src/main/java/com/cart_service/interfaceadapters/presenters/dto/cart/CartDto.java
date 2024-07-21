package com.cart_service.interfaceadapters.presenters.dto.cart;

import com.cart_service.interfaceadapters.presenters.dto.Dto;
import com.cart_service.util.enums.CartStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartDto extends Dto implements Serializable {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String customerId;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private int productsQuantity;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private double cartValue;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime creationDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateDate;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private CartStatus cartStatus;

    private List<ProductReservationDto> productReservation;

}
