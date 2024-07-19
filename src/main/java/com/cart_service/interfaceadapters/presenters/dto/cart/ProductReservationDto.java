package com.cart_service.interfaceadapters.presenters.dto.cart;

import com.cart_service.interfaceadapters.presenters.dto.Dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties({"id"})
public class ProductReservationDto extends Dto implements Serializable {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    String reservationId;

    ProductDetailsDto productDetails;

}
