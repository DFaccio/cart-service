package com.cart_service.service;

import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface ProductService {

    Mono<ProductDto> getProduct(String sku);

    Mono<ReservationDto> postReservation(ReservationDto reservationDto);

}
