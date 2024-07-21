package com.cart_service.interfaceadapters.helper;

import com.cart_service.entities.Cart;
import com.cart_service.interfaceadapters.presenters.CartPresenter;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.service.ProductService;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
public class CartHelper {

    @Resource
    private ProductService productService;

    @Resource
    private CartPresenter cartPresenter;

    public ReservationListDto createReservation(ReservationListDto reservationDto){

        return productService.createReservation(reservationDto);

    }

    public ProductDto getProduct(String sku){

        return productService.getProduct(sku).block();

    }

    public ReservationDto updateReservation(ReservationDto reservationDto){

        return productService.updateReservation(reservationDto);

    }

    public Mono<Page<CartDto>> convert(Flux<Cart> cart, Pageable pageable){

        return cart.map(cartPresenter::convert) // Converter para DTO
                .collectList() // Coletar todos os DTOs em uma lista
                .map(list -> {
                    int start = (int) pageable.getOffset();
                    int end = Math.min(start + pageable.getPageSize(), list.size());
                    List<CartDto> sublist = list.subList(start, end);
                    return new PageImpl<>(sublist, pageable, list.size());
                });

    }
}
