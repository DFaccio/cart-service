package com.cart_service.service.serviceImpl;

import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Qualifier("webbuilder")
    private final WebClient.Builder webBuilder;

    @Qualifier("webclient")
    private final WebClient webClient;

    private static final String PRODUCT_SERVICE_URL = "http://PRODUCT-API/api/v1/product";

    private static final String REAERVATION_SERVICE_URL = "http://PRODUCT-API/products/reservation";

    private static final String PRODUCT_SKU_URI = "/sku/";

    private static final String RESERVATION_SERVICE_URI = "/products/reservation";

    private static final String RESERVATION_CONFIRMATION_SERVICE_URI = "/confirm";

    private static final String RESERVATION_CANCELATION_SERVICE_URI = "/confirm";

    @Override
    public Mono<ProductDto> getProduct(String sku) {

        return webBuilder.build()
                .get()
                .uri(String.format(PRODUCT_SERVICE_URL, PRODUCT_SKU_URI, sku))
                .retrieve()
                .bodyToMono(ProductDto.class);

    }

    @Override
    public ReservationListDto createReservation(ReservationListDto reservationDto){

        return (ReservationListDto) webClient
                .post()
                .uri(String.format(REAERVATION_SERVICE_URL, RESERVATION_SERVICE_URI))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationDto, ReservationListDto.class)
                .retrieve();

    }

    @Override
    public ReservationDto updateReservation(ReservationDto reservationDto){

        return (ReservationDto) webClient
                .put()
                .uri(String.format(RESERVATION_SERVICE_URI, "/id/",reservationDto.getId(), "/quantity/", reservationDto.getQuantity()))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationDto, ReservationDto.class)
                .retrieve();

    }

    @Override
    public void confirmReservation(List<String> reservationIds){

        webClient
                .put()
                .uri(String.format(RESERVATION_SERVICE_URI, RESERVATION_CONFIRMATION_SERVICE_URI))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationIds, ReservationDto.class)
                .retrieve()
                .bodyToMono(ReservationDto.class);

    }

    @Override
    public void cancelReservation(List<String> reservationIds){

        webClient
                .put()
                .uri(String.format(RESERVATION_SERVICE_URI, RESERVATION_CANCELATION_SERVICE_URI))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationIds, ReservationDto.class)
                .retrieve()
                .bodyToMono(ReservationDto.class);

    }

}
