package com.cart_service.service.serviceImpl;

import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("PRODUCT_ADDRESS")
    private static final String PRODUCT_ADRESS = "{PRODUCT_ADDRESS}";

    private static final String PRODUCT_SERVICE_URL = "/api/v1/product";

    private static final String RESERVATION_SERVICE_URL = "/products/reservation";

    private static final String PRODUCT_SKU_URI = "/sku/";

    private static final String RESERVATION_ID_URI = "/id/";

    private static final String RESERVATION_QUANTITY_URI = "/quantity/";

    private static final String RESERVATION_URI = "/products/reservation";

    private static final String RESERVATION_CONFIRMATION_URI = "/confirm";

    private static final String RESERVATION_CANCELLATION_URI = "/cancel";

    @Override
    public Mono<ProductDto> getProduct(String sku) {

        return webBuilder.build()
                .get()
                .uri(String.format(PRODUCT_ADRESS, PRODUCT_SERVICE_URL, PRODUCT_SKU_URI, sku))
                .retrieve()
                .bodyToMono(ProductDto.class);

    }

    @Override
    public ReservationListDto createReservation(ReservationListDto reservationDto){

        return (ReservationListDto) webClient
                .post()
                .uri(String.format(PRODUCT_ADRESS, RESERVATION_SERVICE_URL, RESERVATION_URI))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationDto, ReservationListDto.class)
                .retrieve();

    }

    @Override
    public ReservationDto updateReservation(ReservationDto reservationDto){

        return (ReservationDto) webClient
                .put()
                .uri(String.format(PRODUCT_ADRESS, RESERVATION_URI, RESERVATION_ID_URI ,reservationDto.getId(), RESERVATION_QUANTITY_URI, reservationDto.getQuantity()))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationDto, ReservationDto.class)
                .retrieve();

    }

    @Override
    public void confirmReservation(List<String> reservationIds){

        webClient
                .put()
                .uri(String.format(RESERVATION_URI, RESERVATION_CONFIRMATION_URI))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationIds, ReservationDto.class)
                .retrieve()
                .bodyToMono(ReservationDto.class);

    }

    @Override
    public void cancelReservation(List<String> reservationIds){

        webClient
                .put()
                .uri(String.format(RESERVATION_URI, RESERVATION_CANCELLATION_URI))
                .header("Authorization", "Bearer "+("admin@email.com"))
                .body(reservationIds, ReservationDto.class)
                .retrieve()
                .bodyToMono(ReservationDto.class);

    }

}
