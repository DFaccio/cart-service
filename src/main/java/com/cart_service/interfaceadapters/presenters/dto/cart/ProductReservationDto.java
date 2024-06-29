package com.cart_service.interfaceadapters.presenters.dto.cart;

import com.cart_service.interfaceadapters.presenters.dto.Dto;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    String reservationId;

    ProductDetailsDto productDetailsDto;

}
