package com.cart_service.frameworks.external.inventory;

import com.cart_service.interfaceadapters.presenters.dto.product.AvailabilityDto;
import com.cart_service.interfaceadapters.presenters.dto.product.CategoryInformationDto;
import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;

@Service
@Transactional
@Log4j2
public class ProductServiceInterfaceImpl implements ProductServiceInterface {

    private final WebClient.Builder webClientBuilder;

    private final ObjectMapper mapper;

    @Value("${product.address}")
    private String productAddress;

    private static final String INVENTORY_BASE_URL_PRODUCTS = "/api/v1/product";

    private static final String INVENTORY_BASE_URL_RESERVATION = "/products/reservation";

    @Autowired
    public ProductServiceInterfaceImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.mapper = objectMapper;

        this.mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Override
    public Mono<ProductDto> getProduct(String sku) {
        return webClientBuilder.build()
                .get()
                .uri(productAddress + INVENTORY_BASE_URL_PRODUCTS + "/sku/" + sku)
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Error: " + errorBody))
                        )
                )
                .bodyToMono(JsonNode.class)
                .flatMap(response -> convertResponseToProduct(response, sku));
    }

    private Mono<ProductDto> convertResponseToProduct(JsonNode response, String sku) {
        ProductDto productDto = new ProductDto();

        productDto.setValue(response.get("value").asDouble());
        productDto.setDescription(response.get("description").asText());
        productDto.setId(response.get("id").asText());

        productDto.setCategoryInformationDto(new CategoryInformationDto());

        productDto.getCategoryInformationDto()
                .setAvailabilityDto(new AvailabilityDto(sku, getQuantity(response.get("categoryInformation"), sku)));

        return Mono.just(productDto);
    }

    public Integer getQuantity(JsonNode categoryInformation, String sku) {
        if (categoryInformation.get("availability").isArray()) {
            for (JsonNode availability : categoryInformation.withArray("availability")) {
                if (availability.get("sku").asText().equals(sku)) {
                    return availability.get("quantity").asInt();
                }
            }

            return 0;
        }

        return categoryInformation.get("availability").get("quantity").asInt();
    }

    @Override
    public ReservationListDto createReservation(ReservationListDto reservationDto) throws IOException {
        JsonNode response = webClientBuilder.build()
                .post()
                .uri(productAddress + INVENTORY_BASE_URL_RESERVATION)
                .body(BodyInserters.fromValue(mapper.writeValueAsString(reservationDto.getReservations())))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Error: " + errorBody))
                        )
                )
                .bodyToMono(JsonNode.class)
                .block();

        List<ReservationDto> reservations = mapper.readValue(response.traverse(),
                new TypeReference<List<ReservationDto>>() {
                });

        return new ReservationListDto(reservations);
    }

    @Override
    public ReservationDto updateReservation(ReservationDto reservationDto) throws IOException {
        JsonNode node = webClientBuilder.build()
                .put()
                .uri(productAddress + INVENTORY_BASE_URL_RESERVATION + "/id/" + reservationDto.getId() + "/quantity/" + reservationDto.getQuantity())
                .body(BodyInserters.fromValue(reservationDto))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Error: " + errorBody))
                        )
                ).bodyToMono(JsonNode.class).block();

        return mapper.readValue(node.traverse(), ReservationDto.class);
    }

    @Override
    public ReservationListDto confirmReservation(List<String> reservationIds) throws IOException {
        JsonNode response = webClientBuilder.build()
                .put()
                .uri(productAddress + INVENTORY_BASE_URL_RESERVATION + "/confirm")
                .body(BodyInserters.fromValue(reservationIds))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Error: " + errorBody))
                        )
                )
                .bodyToMono(JsonNode.class)
                .block();

        List<ReservationDto> reservations = mapper.readValue(response.traverse(),
                new TypeReference<List<ReservationDto>>() {
                });

        return new ReservationListDto(reservations);
    }

    @Override
    public ReservationListDto cancelReservation(List<String> reservationIds) throws IOException {
        JsonNode response = webClientBuilder.build()
                .put()
                .uri(productAddress + INVENTORY_BASE_URL_RESERVATION + "/cancel")
                .body(BodyInserters.fromValue(reservationIds))
                .retrieve()
                .onStatus(HttpStatusCode::isError, clientResponse -> clientResponse
                        .bodyToMono(String.class)
                        .flatMap(errorBody -> Mono.error(new RuntimeException("Error: " + errorBody))
                        )
                )
                .bodyToMono(JsonNode.class)
                .block();

        List<ReservationDto> reservations = mapper.readValue(response.traverse(),
                new TypeReference<List<ReservationDto>>() {
                });

        return new ReservationListDto(reservations);
    }
}