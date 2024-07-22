package com.cart_service.service.serviceImpl;

import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.service.ProductService;
import org.springframework.http.HttpMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    @Qualifier("webClientBuilder")
    @Autowired
    private WebClient.Builder webClientBuilder;

    @Qualifier("webclient")
    @Autowired
    private WebClient webClient;

    @Autowired
    private WebClient webClientProdutos;

    @Value("${product.address}")
    private String productAddress;

    private static final String PRODUCT_SERVICE_URL = "api/v1/product";

    private static final String RESERVATION_SERVICE_URL = "products/reservation";

    private static final String PRODUCT_SKU_URI = "/sku/";

    private static final String RESERVATION_ID_URI = "/id/";

    private static final String RESERVATION_QUANTITY_URI = "/quantity/";

    private static final String RESERVATION_CONFIRMATION_URI = "/confirm";

    private static final String RESERVATION_CANCELLATION_URI = "/cancel";

    @Override
    public Mono<ProductDto> getProduct(String sku) {

        return this.webClientProdutos
                .method(HttpMethod.GET)
                .uri(productAddress + PRODUCT_SERVICE_URL + PRODUCT_SKU_URI + sku)
                .retrieve()
                .bodyToMono(ProductDto.class);
//
//        return webClientBuilder.build()
//                .get()
//                .uri(String.format(productAddress + PRODUCT_SERVICE_URL + PRODUCT_SKU_URI + sku))
//                .retrieve()
//                .bodyToMono(ProductDto.class);

    }

    @Override
    public ReservationListDto createReservation(ReservationListDto reservationDto){

        return this.webClientProdutos
                .method(HttpMethod.POST)
                .uri(RESERVATION_SERVICE_URL)
                .body(BodyInserters.fromValue(reservationDto))
//                .body(Mono.just(reservationDto), ReservationListDto.class)
                .retrieve()
                .bodyToMono(ReservationListDto.class).block();

//        return webClientBuilder.build()
//                .post()
//                .uri(String.format(productAddress + RESERVATION_SERVICE_URL))
//                .header("Authorization", "Bearer "+("admin@email.com"))
//                .body(Mono.just(reservationDto), ReservationListDto.class)
//                .retrieve()
//                .bodyToMono(ReservationListDto.class).block();

    }

    @Override
    public ReservationDto updateReservation(ReservationDto reservationDto){

        return this.webClientProdutos
                .method(HttpMethod.PUT)
                .uri(RESERVATION_SERVICE_URL + RESERVATION_ID_URI  + reservationDto.getId() + RESERVATION_QUANTITY_URI + reservationDto.getQuantity())
                .body(BodyInserters.fromValue(reservationDto))
//                .body(Mono.just(reservationDto), ReservationDto.class)
                .retrieve()
                .bodyToMono(ReservationDto.class).block();

//        return webClientBuilder.build()
//                .put()
//                .uri(String.format(productAddress + RESERVATION_SERVICE_URL + RESERVATION_ID_URI  + reservationDto.getId() + RESERVATION_QUANTITY_URI + reservationDto.getQuantity()))
//                .header("Authorization", "Bearer "+("admin@email.com"))
//                .body(Mono.just(reservationDto), ReservationDto.class)
//                .retrieve()
//                .bodyToMono(ReservationDto.class).block();

    }

    @Override
    public void confirmReservation(List<String> reservationIds){

        this.webClientProdutos
            .method(HttpMethod.PUT)
            .uri(RESERVATION_SERVICE_URL + RESERVATION_CONFIRMATION_URI)
            .body(BodyInserters.fromValue(reservationIds))
//            .body(Mono.just(reservationIds), ReservationListDto.class)
            .retrieve();

//        webClient
//                .put()
//                .uri(String.format(productAddress + RESERVATION_SERVICE_URL + RESERVATION_CONFIRMATION_URI))
//                .header("Authorization", "Bearer "+("admin@email.com"))
//                .body(Mono.just(reservationIds), ReservationDto.class)
//                .retrieve();

    }

    @Override
    public void cancelReservation(List<String> reservationIds){

        this.webClientProdutos
                .method(HttpMethod.PUT)
                .uri(RESERVATION_SERVICE_URL + RESERVATION_CANCELLATION_URI)
                .body(BodyInserters.fromValue(reservationIds))
//                .body(Mono.just(reservationIds), ReservationListDto.class)
                .retrieve();

//        webClient
//                .put()
//                .uri(String.format(productAddress + RESERVATION_SERVICE_URL + RESERVATION_CANCELLATION_URI))
//                .header("Authorization", "Bearer "+("admin@email.com"))
//                .body(Mono.just(reservationIds), ReservationDto.class)
//                .retrieve();

    }

}