package com.cart_service.frameworks.external.inventory;

import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
public interface ProductServiceInterface {

    Mono<ProductDto> getProduct(String sku);

    ReservationListDto createReservation(ReservationListDto reservationDto) throws IOException;

    ReservationDto updateReservation(ReservationDto reservationDto) throws IOException;

    ReservationListDto confirmReservation(List<String> reservationIds) throws IOException;

    ReservationListDto cancelReservation(List<String> reservationIds) throws IOException;

}
