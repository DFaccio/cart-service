package com.cart_service.interfaceadapters.helper;

import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class CartHelper {

    @Resource
    private ProductService productService;

    public ReservationListDto createReservation(ReservationListDto reservationDto){

        return productService.createReservation(reservationDto);

    }

    public ProductDto getProduct(String sku){

        return productService.getProduct(sku).block();

    }

}
