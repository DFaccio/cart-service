package com.cart_service.service;

import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface ProductService {

    Mono<ProductDto> getProduct(String sku);

    ReservationListDto createReservation(ReservationListDto reservationDto);

    ReservationDto updateReservation(ReservationDto reservationDto);

    void confirmReservation(List<String> reservationIds);

    void cancelReservation(List<String> reservationIds);

}
