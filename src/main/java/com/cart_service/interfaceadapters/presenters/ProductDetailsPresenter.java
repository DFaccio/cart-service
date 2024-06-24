package com.cart_service.interfaceadapters.presenters;

import com.cart_service.entities.ProductDetails;
import com.cart_service.interfaceadapters.presenters.dto.ProductDetailsDto;

public class ProductDetailsPresenter implements Presenter<ProductDetails, ProductDetailsDto>{

    @Override
    public ProductDetailsDto convert(ProductDetails document){

        ProductDetailsDto productDetailsDto = new ProductDetailsDto();

        productDetailsDto.setSku(document.getSku());
        productDetailsDto.setName(document.getName());
        productDetailsDto.setPrice(document.getPrice());
        productDetailsDto.setQuantity(document.getQuantity());
        productDetailsDto.setTotal(document.getTotal());

        return productDetailsDto;

    }

    @Override
    public ProductDetails convert(ProductDetailsDto dto){

        ProductDetails productDetails = new ProductDetails();

        productDetails.setSku(dto.getSku());
        productDetails.setName(dto.getName());
        productDetails.setPrice(dto.getPrice());
        productDetails.setQuantity(dto.getQuantity());
        productDetails.setTotal(dto.getTotal());

        return productDetails;

    }

}
