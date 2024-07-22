package com.cart_service.interfaceadapters.helper;

import com.cart_service.entities.Cart;
import com.cart_service.frameworks.external.inventory.ProductServiceInterface;
import com.cart_service.interfaceadapters.presenters.CartPresenter;
import com.cart_service.interfaceadapters.presenters.dto.cart.CartDto;
import com.cart_service.interfaceadapters.presenters.dto.product.ProductDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationDto;
import com.cart_service.interfaceadapters.presenters.dto.reservation.ReservationListDto;
import com.cart_service.util.pagination.Pagination;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartHelper {

    @Resource
    private ProductServiceInterface productService;

    @Resource
    private CartPresenter cartPresenter;

    public ReservationListDto createReservation(ReservationListDto reservationDto) throws IOException {
        return productService.createReservation(reservationDto);

    }

    public ProductDto getProduct(String sku) {
        return productService.getProduct(sku).block();
    }

    public ReservationDto updateReservation(ReservationDto reservationDto) throws IOException {
        return productService.updateReservation(reservationDto);
    }

    public Mono<Page<CartDto>> convertFluxToMonoPage(Flux<Cart> cart, Pagination page) {
        Pageable pageable = PageRequest.of(page.getPage(), page.getPageSize());

        return cart.collectList()
                .flatMap(carts -> {
                    int total = carts.size();
                    List<Cart> paginatedList = carts.stream()
                            .skip(pageable.getOffset())
                            .limit(pageable.getPageSize())
                            .toList();

                    List<CartDto> cartDtos = paginatedList.stream()
                            .map(cartPresenter::convert)
                            .collect(Collectors.toList());

                    Page<CartDto> pageCartDtos = new PageImpl<>(cartDtos, pageable, total);
                    return Mono.just(pageCartDtos);
                });

    }
}
