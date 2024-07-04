package com.cart_service.service.serviceImpl;

import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Qualifier("webbuilder")
    private final WebClient.Builder webBuilder;

    @Qualifier("webclient")
    private final WebClient webClient;

    private static final String PRODUCT_SERVICE_URL = "/api/v1/product/sku/";

    private static final String RESERVATION_SERVICE_URL = "/products/reservation";

    @Override
    public Mono<ProductDto> getProduct(String sku) {

        return webBuilder.build()
                .get()
                .uri(String.format(PRODUCT_SERVICE_URL, sku))
                .retrieve()
                .bodyToMono(ProductDto.class);

    }

    @Override
    public Mono<ReservationDto> postReservation(ReservationDto reservationDto){

        return webClient
                .post()
                .uri(String.format(RESERVATION_SERVICE_URL))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationDto, ReservationDto.class)
                .retrieve()
                .bodyToMono(ReservationDto.class);

    }

}
