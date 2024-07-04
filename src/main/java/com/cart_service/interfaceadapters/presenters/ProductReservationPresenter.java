package com.cart_service.interfaceadapters.presenters;

import com.cart_service.entities.ProductReservation;
import com.cart_service.interfaceadapters.presenters.dto.cart.ProductReservationDto;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class ProductReservationPresenter implements Presenter<ProductReservation, ProductReservationDto>{

    @Resource
    private ProductDetailsPresenter productDetailsPresenter;

    @Override
    public ProductReservationDto convert(ProductReservation document) {

        ProductReservationDto productReservationDto = new ProductReservationDto();

        productReservationDto.setReservationId(document.getReservationId());
        productReservationDto.setProductDetailsDto(productDetailsPresenter.convert(document.getProductDetails()));

        return productReservationDto;

    }

    @Override
    public ProductReservation convert(ProductReservationDto dto) {

        ProductReservation productReservation = new ProductReservation();

        productReservation.setReservationId(dto.getReservationId());
        productReservation.setProductDetails(productDetailsPresenter.convert(dto.getProductDetailsDto()));

        return productReservation;

    }
}
